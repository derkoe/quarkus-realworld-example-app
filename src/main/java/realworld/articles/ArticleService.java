package realworld.articles;

import realworld.articles.web.CreateArticle;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ArticleService {
    public Article create(CreateArticle createArticle) {
        Article article = new Article();
        article.title = createArticle.title;
        article.description = createArticle.description;
        article.body = createArticle.body;
        article.persist();
        return article;
    }
}
