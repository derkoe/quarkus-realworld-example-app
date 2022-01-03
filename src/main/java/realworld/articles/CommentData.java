package realworld.articles;

import java.time.Instant;
import java.util.UUID;

public record CommentData(UUID id, String body, Instant createdAt, Instant updatedAt, String author) {
}
