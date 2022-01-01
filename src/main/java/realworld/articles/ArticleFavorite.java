package realworld.articles;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class ArticleFavorite {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(name = "article_id")
    private UUID articleId;
    @Column(name = "user_id")
    private UUID userId;

    public ArticleFavorite(UUID articleId, UUID userId) {
        this.articleId = articleId;
        this.userId = userId;
    }
}
