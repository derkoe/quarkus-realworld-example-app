package realworld.users;

import io.quarkus.security.AuthenticationFailedException;
import io.smallrye.jwt.build.Jwt;
import realworld.users.web.UserLogin;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@ApplicationScoped
public class UserService {

    @Transactional
    public UserWithToken createUser(UserRegistration registration) {
        if (User.findByUsername(registration.username).isPresent()) {
            throw new IllegalArgumentException("Username already taken");
        }
        if (User.findByEmail(registration.email).isPresent()) {
            throw new IllegalArgumentException("User with email already exists");
        }
        User user = new User();
        user.username = registration.username;
        user.password = registration.password;
        user.email = registration.email;
        user.persist();
        return map(user);
    }

    @Transactional
    public UserWithToken updateUser(UUID id, UserUpdate update) {
        User user = User.findById(id);
        if (update.email != null)
            user.email = update.email;
        if (update.username != null)
            user.username = update.username;
        if (update.password != null)
            user.password = update.password;
        if (update.bio != null)
            user.bio = update.bio;
        if (update.image != null)
            user.image = update.image;
        return map(user);
    }

    public UserWithToken login(UserLogin userLogin) {
        Optional<User> user = User.findByEmail(userLogin.email);
        return user.map(this::map).orElseThrow(AuthenticationFailedException::new);
    }

    public Optional<UserWithToken> getUser(UUID id) {
        Optional<User> user = User.findByIdOptional(id);
        return user.map(this::map);
    }

    private UserWithToken map(User user) {
        String token = Jwt.issuer("https://quarkus-realworld.derkoe.dev")
                .subject(user.id.toString())
                .upn(user.email)
                .issuedAt(Instant.now())
                .expiresIn(Duration.ofMinutes(10))
                .groups(Set.of("User"))
                .sign();

        return new UserWithToken(user.username, user.email, user.bio, user.image, token);
    }
}
