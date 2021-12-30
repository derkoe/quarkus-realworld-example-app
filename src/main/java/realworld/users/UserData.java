package realworld.users;

import lombok.Builder;

@Builder
public record UserData(String username, String email, String bio, String image, String token) {
}
