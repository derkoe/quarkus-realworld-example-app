package realworld.users;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Optional;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User extends PanacheEntityBase {
    @Id
    @GeneratedValue
    public UUID id;
    @NotEmpty
    public String username;
    @NotEmpty
    public String password;
    @Email
    @NotEmpty
    public String email;
    public String bio;
    public String image;

    public static Optional<User> findByUsername(String username) {
        return find("username", username).singleResultOptional();
    }

    public static Optional<User> findByEmail(String email) {
        return find("email", email).singleResultOptional();
    }
}
