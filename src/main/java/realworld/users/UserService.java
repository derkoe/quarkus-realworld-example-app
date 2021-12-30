package realworld.users;

import io.quarkus.security.AuthenticationFailedException;
import io.smallrye.jwt.build.Jwt;
import realworld.users.web.UserLogin;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;

    @Transactional
    public UserData createUser(UserRegistration registration) {
        if (userRepository.findByUsername(registration.username).isPresent()) {
            throw new IllegalArgumentException("Username already taken");
        }
        if (userRepository.findByEmail(registration.email).isPresent()) {
            throw new IllegalArgumentException("User with email already exists");
        }
        User user = new User();
        user.setUsername(registration.username);
        user.setPassword(registration.password); // TODO hash password
        user.setEmail(registration.email);
        userRepository.persist(user);
        return map(user);
    }

    @Transactional
    public UserData updateUser(UUID id, UserUpdate update) {
        User user = userRepository.findById(id);
        if (update.email() != null)
            user.setEmail(update.email());
        if (update.username() != null)
            user.setUsername(update.username());
        if (update.password() != null)
            user.setPassword(update.password());
        if (update.bio() != null)
            user.setBio(update.bio());
        if (update.image() != null)
            user.setImage(update.image());
        return map(user);
    }

    public UserData login(UserLogin userLogin) {
        Optional<User> user = userRepository.findByEmail(userLogin.email);
        return user.map(UserService::map).orElseThrow(AuthenticationFailedException::new);
    }

    public Optional<UserData> getUser(UUID id) {
        Optional<User> user = userRepository.findByIdOptional(id);
        return user.map(UserService::map);
    }

    public static UserData map(User user) {
        String token = Jwt.issuer("https://quarkus-realworld.derkoe.dev")
                .subject(user.getId().toString())
                .upn(user.getEmail())
                .issuedAt(Instant.now())
                .expiresIn(Duration.ofMinutes(10))
                .groups(Set.of("User"))
                .sign();

        return UserData.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .bio(user.getBio())
                .image(user.getImage())
                .token(token)
                .build();
    }
}
