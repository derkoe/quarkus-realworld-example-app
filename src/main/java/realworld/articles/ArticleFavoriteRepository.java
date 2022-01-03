package realworld.articles;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

import static io.quarkus.panache.common.Parameters.with;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ArticleFavoriteRepository implements PanacheRepository<ArticleFavorite> {
    public void delete(Article article, UUID userId) {
        delete("DELETE FROM ArticleFavorite af WHERE af.article.id = :articleId AND af.userId = :userId",
                with("articleId", article.getId()).and("userId", userId));
    }

    public Optional<ArticleFavorite> findByArticleIdAndUserId(UUID articleId, UUID userId) {
        return find("SELECT af FROM ArticleFavorite af WHERE af.article.id = :articleId AND af.userId = :userId",
                with("articleId", articleId).and("userId", userId))
                        .singleResultOptional();
    }

    public long countByArticle(Article article) {
        return count("article.id = :articleId",
                with("articleId", article.getId()));
    }
}
