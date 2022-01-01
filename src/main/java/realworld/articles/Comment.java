package realworld.articles;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Instant;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue
    private UUID id;
    private String body;
    private UUID userId;
    private UUID articleId;
    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    public Comment(String body, UUID userId, UUID articleId) {
        this.body = body;
        this.userId = userId;
        this.articleId = articleId;
    }
}
