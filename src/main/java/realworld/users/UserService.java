package realworld.users;

import io.smallrye.jwt.build.Jwt;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.time.Duration;
import java.util.Set;

@ApplicationScoped
public class UserService {

    @Transactional
    UserWithToken createUser(UserRegistration registration) {
        User user = new User();
        user.username = registration.username;
        user.password = registration.password;
        user.email = registration.email;
        User.persist(user);
        String token = Jwt.issuer("https://quarkus-realworld.derkoe.dev")
                .subject(user.id.toString())
                .upn(user.email)
                .expiresIn(Duration.ofDays(1))
                .groups(Set.of("User"))
                .sign();
        return new UserWithToken(user.username, user.email, token);
    }
}
