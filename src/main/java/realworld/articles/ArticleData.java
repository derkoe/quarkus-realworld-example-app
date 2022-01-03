package realworld.articles;

import java.time.Instant;
import java.util.List;

import lombok.Builder;

@Builder
public record ArticleData(String id, String slug, String title, String description, String body, boolean favorited,
                          int favoritesCount, Instant createdAt, Instant updatedAt, List<String> tagList,
                          String author) {
}
