package realworld.articles;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "articles_favorites")
@NoArgsConstructor
@Getter
@Setter
public class ArticleFavorite {
    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;
    @Column(name = "user_id")
    private UUID userId;

    public ArticleFavorite(Article article, UUID userId) {
        this.article = article;
        this.userId = userId;
    }
}
