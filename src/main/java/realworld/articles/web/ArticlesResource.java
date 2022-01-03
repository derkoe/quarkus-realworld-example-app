package realworld.articles.web;

import realworld.articles.ArticleData;
import realworld.articles.ArticleService;
import realworld.articles.CommentData;
import realworld.articles.CreateArticle;
import realworld.security.UserHelper;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.List;
import java.util.Map;

@Path("/articles")
@Produces(MediaType.APPLICATION_JSON)
public class ArticlesResource {

    @Inject
    ArticleService articleService;

    @GET
    public ArticlesResponse all(@QueryParam("author") String author, @QueryParam("tag") String tag) {
        List<ArticleData> articles = articleService.search(author, tag);
        return new ArticlesResponse(articles, articles.size());
    }

    @GET
    @Path("/{slug}")
    public ArticleResponse articleBySlug(@PathParam("slug") String slug, @Context SecurityContext sec) {
        return new ArticleResponse(articleService.findBySlug(slug, UserHelper.getUserId(sec)));
    }

    @DELETE
    @Path("/{slug}")
    @RolesAllowed("User")
    public ArticleResponse delete(@PathParam("slug") String slug, @Context SecurityContext sec) {
        return new ArticleResponse(articleService.deleteBySlug(slug, UserHelper.getUserId(sec)));
    }

    @GET
    @Path("/feed")
    public ArticlesResponse feed(@QueryParam("offset") Integer offset, @QueryParam("limit") Integer limit) {
        limit = limit == null ? 20 : limit;
        offset = offset == null ? 1 : offset;
        List<ArticleData> articles = articleService.feed(offset, limit);
        return new ArticlesResponse(articles, articles.size());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("User")
    public ArticleResponse create(ArticleRequest<CreateArticle> createArticle, @Context SecurityContext sec) {
        return new ArticleResponse(articleService.create(createArticle.article, UserHelper.getUserId(sec)));
    }

    @PUT
    @Path("/{slug}")
    @RolesAllowed("User")
    public ArticleResponse update(@PathParam("slug") String slug, ArticleRequest<CreateArticle> createArticle, @Context SecurityContext sec) {
        return new ArticleResponse(articleService.update(slug, createArticle.article, UserHelper.getUserId(sec)));
    }

    @POST
    @Path("/{slug}/favorite")
    @RolesAllowed("User")
    public ArticleResponse favor(@PathParam("slug") String slug, @Context SecurityContext sec) {
        return new ArticleResponse(articleService.addFavorite(slug, UserHelper.getUserId(sec)));
    }

    @DELETE
    @Path("/{slug}/favorite")
    @RolesAllowed("User")
    public ArticleResponse unfavor(@PathParam("slug") String slug, @Context SecurityContext sec) {
        return new ArticleResponse(articleService.removeFavorite(slug, UserHelper.getUserId(sec)));
    }

    @POST
    @Path("/{slug}/comments")
    @RolesAllowed("User")
    public Map<String, CommentData> comment(@PathParam("slug") String slug, CommentRequest commentRequest, @Context SecurityContext sec) {
        return Map.of("comment", articleService.comment(slug, commentRequest.comment(), UserHelper.getUserId(sec)));
    }

    @GET
    @Path("/{slug}/comments")
    public CommentsResponse comments(@PathParam("slug") String slug) {
        List<CommentData> comments = articleService.comments(slug);
        return new CommentsResponse(comments, comments.size());
    }
}
