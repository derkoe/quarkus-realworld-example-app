package realworld.articles;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import realworld.users.User;

import javax.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "articles")
@NoArgsConstructor
@Getter
@Setter
public class Article {
    @Id
    @GeneratedValue
    private UUID id;
    private String slug;
    @ManyToOne
    private User author;
    private String title;
    private String description;
    private String body;
    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
    @ManyToMany
    private Set<Tag> tags = new HashSet<>();
}
