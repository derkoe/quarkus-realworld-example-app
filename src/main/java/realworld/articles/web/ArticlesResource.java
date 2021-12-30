package realworld.articles.web;

import realworld.articles.ArticleData;
import realworld.articles.ArticleService;
import realworld.articles.CreateArticle;
import realworld.security.UserHelper;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

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
    public ArticleResponse articleBySlug(@PathParam("slug") String slug) {
        return new ArticleResponse(articleService.findBySlug(slug));
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
}
