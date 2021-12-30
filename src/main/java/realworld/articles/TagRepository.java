package realworld.articles;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class TagRepository implements PanacheRepository<Tag> {
    public Optional<Tag> findByName(String name) {
        return find("name", name).singleResultOptional();
    }
}
