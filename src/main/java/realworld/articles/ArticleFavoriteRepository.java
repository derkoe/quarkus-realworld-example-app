package realworld.articles;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ArticleFavoriteRepository implements PanacheRepository<ArticleFavorite> {
    public void delete(Article article, UUID userId) {
        delete("DELETE FROM ArticleFavorite af WHERE af.articleId = :articleId AND af.userId = :userId",
                Parameters.with("articleId", article.getId()).and("userId", userId));
    }

    public Optional<ArticleFavorite> findByArticleIdAndUserId(UUID articleId, UUID userId) {
        return find("SELECT af FROM ArticleFavorite af WHERE af.articleId = :articleId AND af.userId = :userId",
                Parameters.with("articleId", articleId).and("userId", userId))
                        .singleResultOptional();
    }
}
