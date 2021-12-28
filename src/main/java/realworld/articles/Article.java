package realworld.articles;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "articles")
public class Article extends PanacheEntityBase {
    @Id
    @GeneratedValue
    public UUID id;
    public String slug;
    public String title;
    public String description;
    public String body;
    @Column(name = "created_at")
    public LocalDateTime createdAt;
    @Column(name = "updated_at")
    public LocalDateTime updatedAt;
}
