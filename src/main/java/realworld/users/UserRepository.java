package realworld.users;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

    public Optional<User> findByUsername(String username) {
        return find("username", username).singleResultOptional();
    }

    public Optional<User> findByEmail(String email) {
        return find("email", email).singleResultOptional();
    }

    public User findById(UUID id) {
        return find("id", id).singleResult();
    }

    public Optional<User> findByIdOptional(UUID id) {
        return find("id", id).singleResultOptional();
    }
}
