package realworld.users;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import io.quarkus.security.AuthenticationFailedException;
import io.smallrye.jwt.build.Jwt;
import realworld.users.web.UserLogin;

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

    public Optional<ProfileData> getUserProfile(UUID userId, String username) {
        Optional<User> user = userRepository.find("username", username).firstResultOptional();
        return user.map(u -> mapProfile(u, userId));
    }

    @Transactional
    public ProfileData addFollower(UUID userId, String username) {
        User follower = userRepository.findById(userId);
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(u -> {
            u.getFollowers().add(follower);
            return mapProfile(user.get(), userId);
        }).orElse(null);
    }

    @Transactional
    public ProfileData removeFollower(UUID userId, String username) {
        User follower = userRepository.findById(userId);
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(u -> {
            u.getFollowers().remove(follower);
            return mapProfile(user.get(), userId);
        }).orElse(null);
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

    public static ProfileData mapProfile(User user, UUID userId) {
        return ProfileData.builder()
                .username(user.getUsername())
                .bio(user.getBio())
                .image(user.getImage())
                .following(user.getFollowers().stream().anyMatch(u -> u.getId().equals(userId)))
                .build();
    }
}
