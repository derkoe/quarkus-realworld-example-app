package realworld.articles;

import java.util.Set;

public record CreateArticle(String title, String description, String body, Set<String> tagList) {
}
