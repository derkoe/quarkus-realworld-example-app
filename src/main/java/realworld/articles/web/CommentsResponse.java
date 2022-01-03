package realworld.articles.web;

import java.util.List;

import realworld.articles.CommentData;

public record CommentsResponse(List<CommentData> comments, Integer commentsCount) {
}
