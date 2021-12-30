package realworld.articles.web;

import realworld.articles.ArticleData;

record ArticlesResponse(Iterable<ArticleData> articles, Integer articlesCount) {
}
