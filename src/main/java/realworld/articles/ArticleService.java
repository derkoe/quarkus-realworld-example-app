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

    @Inject
    CommentRepository commentRepository;

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

    public ArticleData findBySlug(String slug, UUID userId) {
        Article article = articleRepository.findBySlug(slug);
        return mapWithFavorites(article, userId);
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
        articleFavoriteRepository.persist(new ArticleFavorite(article, userId));
        return mapWithFavorites(articleRepository.findBySlug(slug), userId);
    }

    @Transactional
    public ArticleData removeFavorite(String slug, UUID userId) {
        Article article = articleRepository.findBySlug(slug);
        articleFavoriteRepository.delete(article, userId);
        return map(article);
    }

    @Transactional
    public ArticleData deleteBySlug(String slug, UUID userId) {
        Article article = articleRepository.findBySlug(slug);
        if (!article.getAuthor().getId().equals(userId)) {
            throw new ForbiddenException();
        }
        articleRepository.delete(article);
        return map(article);
    }

    private String slugify(String title) {
        return title.replaceAll("[^a-zA-Z0-9]", "-").toLowerCase();
    }

    private ArticleData map(Article article) {
        return mapWithFavorites(article, null);
    }

    private ArticleData mapWithFavorites(Article article, UUID userId) {
        if (article == null) {
            return null;
        }

        int favoriteCount = article.getFavorites().size();
        boolean favorited = false;
        if (userId != null) {
            favorited = article.getFavorites().stream().filter(af -> af.getUserId().equals(userId)).count() > 0;
        }

        List<String> tags = article.getTags().stream().map(Tag::getName).sorted().toList();
        UserData author = UserService.map(article.getAuthor());
        return ArticleData.builder().id(article.getId().toString()).slug(article.getSlug()).title(article.getTitle())
                .description(article.getDescription())
                .body(article.getBody())
                .favorited(favorited)
                .favoritesCount(favoriteCount)
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .tagList(tags)
                .author(author.username())
                .build();
    }

    @Transactional
    public CommentData comment(String slug, CommentData comment, UUID userId) {
        Article article = articleRepository.findBySlug(slug);
        Comment c = new Comment(comment.body(), userId, article.getId());
        commentRepository.persistAndFlush(c);
        return new CommentData(c.getId(), c.getBody(), c.getCreatedAt(), c.getUpdatedAt(), c.getUserId().toString());
    }

    public List<CommentData> comments(String slug) {
        Article article = articleRepository.findBySlug(slug);
        return commentRepository.list("articleId", article.getId())
            .stream()
            .map(c -> new CommentData(c.getId(), c.getBody(), c.getCreatedAt(), c.getUpdatedAt(), c.getUserId().toString()))
            .toList();
    }
}
