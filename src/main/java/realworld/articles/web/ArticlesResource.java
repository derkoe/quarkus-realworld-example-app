package realworld.articles.web;

import realworld.articles.Article;
import realworld.articles.ArticleService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.List;

@Path("/articles")
public class ArticlesResource {

    @Inject
    private ArticleService articleService;

    @GET
    public ArticlesResponse all() {
        List<Article> articles = Article.listAll();
        return new ArticlesResponse(articles, articles.size());
    }

    @POST
    public ArticleResponse create(ArticleRequest<CreateArticle> createArticle) {
        return new ArticleResponse(articleService.create(createArticle.article));
    }
}

class ArticlesResponse {
    public final Iterable<Article> articles;
    public final Integer articlesCount;

    ArticlesResponse(Iterable<Article> articles, Integer articlesCount) {
        this.articles = articles;
        this.articlesCount = articlesCount;
    }
}

class ArticleResponse {
    public final Article article;

    ArticleResponse(Article article) {
        this.article = article;
    }
}
