package realworld.articles;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class Article extends PanacheEntityBase {
    @Id
    public UUID id;
    public String slug;
    public String title;
}
