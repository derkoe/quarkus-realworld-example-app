package realworld.articles;

import lombok.Builder;
import realworld.users.UserData;

import java.time.Instant;
import java.util.List;

@Builder
public record ArticleData(String id, String slug, String title, String description, String body, boolean favorited,
                          int favoritesCount, Instant createdAt, Instant updatedAt, List<String> tagList,
                          UserData author) {
}
