package realworld.users;

import lombok.Builder;

@Builder
public record ProfileData(String username, String bio, String image, Boolean following) {
}
