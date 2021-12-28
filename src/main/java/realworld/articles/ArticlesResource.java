package realworld.articles;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/articles")
public class ArticlesResource {

    @GET
    public Iterable<Article> all() {
        return Article.listAll();
    }
}
