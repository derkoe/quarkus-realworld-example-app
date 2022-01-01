package realworld.articles;

import realworld.users.UserData;
import realworld.users.UserRepository;
import realworld.users.UserService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.ForbiddenException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
public class ArticleService {

    @Inject
    ArticleRepository articleRepository;

    @Inject
    TagRepository tagRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    ArticleFavoriteRepository articleFavoriteRepository;

    @Transactional
    public ArticleData create(CreateArticle createArticle, UUID userId) {
        Article article = new Article();
        article.setTitle(createArticle.title());
        article.setSlug(slugify(createArticle.title()));
        article.setAuthor(userRepository.findById(userId));
        article.setDescription(createArticle.description());
        article.setBody(createArticle.body());
        article.setTags(createArticle.tagList()
                .stream()
                .map(name -> tagRepository.findByName(name).orElseGet(() -> {
                    Tag tag = new Tag(name);
                    tagRepository.persist(tag);
                    return tag;
                }))
                .collect(Collectors.toSet()));
        articleRepository.persistAndFlush(article);
        return map(article);
    }

    private ArticleData map(Article article) {
        return mapWithFavorites(article, 0, false);
    }

    private ArticleData mapWithFavorites(Article article, int favoritesCount, boolean favorited) {
        if (article == null) {
            return null;
        }
        List<String> tags = article.getTags().stream().map(Tag::getName).sorted().toList();
        UserData author = UserService.map(article.getAuthor());
        return ArticleData.builder().id(article.getId().toString()).slug(article.getSlug()).title(article.getTitle())
                .description(article.getDescription())
                .body(article.getBody())
                .favorited(favorited)
                .favoritesCount(favoritesCount)
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .tagList(tags)
                .author(author)
                .build();
    }

    @Transactional
    public List<ArticleData> search(String author, String tag) {
        Stream<Article> articles;
        if (author != null) {
            articles = articleRepository.findByAuthorUserName(author);
        } else if (tag != null) {
            articles = articleRepository.findByTagName(tag);
        } else {
            articles = articleRepository.streamAll();
        }
        return articles.map(this::map).toList();
    }

    public List<ArticleData> feed(int offset, int limit) {
        return articleRepository.findAll().page(offset, limit).stream().map(this::map).toList();
    }

    private String slugify(String title) {
        return title.replaceAll("[^a-zA-Z0-9]", "-").toLowerCase();
    }

    public ArticleData findBySlug(String slug, UUID userId) {
        Article article = articleRepository.findBySlug(slug);
        long favouriteCount = articleFavoriteRepository.count("articleId", article.getId());
        boolean favorited = false;
        if (userId != null) {
            favorited = articleFavoriteRepository.findByArticleIdAndUserId(article.getId(), userId).isPresent();
        }
        return mapWithFavorites(article, (int) favouriteCount, favorited);
    }

    @Transactional
    public ArticleData update(String slug, CreateArticle changedArticle, UUID userId) {
        Article article = articleRepository.findBySlug(slug);
        if (!article.getAuthor().getId().equals(userId)) {
            throw new ForbiddenException();
        }
        if (changedArticle.title() != null) {
            article.setTitle(changedArticle.title());
        }
        if (changedArticle.description() != null) {
            article.setDescription(changedArticle.description());
        }
        if (changedArticle.body() != null) {
            article.setBody(changedArticle.body());
        }
        return map(article);
    }

    @Transactional
    public ArticleData addFavorite(String slug, UUID userId) {
        Article article = articleRepository.findBySlug(slug);
        articleFavoriteRepository.persist(new ArticleFavorite(article.getId(), userId));
        long favouriteCount = articleFavoriteRepository.count("articleId", article.getId());
        return mapWithFavorites(article, (int) favouriteCount, true);
    }

    @Transactional
    public ArticleData removeFavorite(String slug, UUID userId) {
        Article article = articleRepository.findBySlug(slug);
        articleFavoriteRepository.delete(article, userId);
        return map(article);
    }
}
