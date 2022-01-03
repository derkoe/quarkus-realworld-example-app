package realworld.articles.web;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import realworld.articles.Tag;
import realworld.articles.TagRepository;

@Path("/tags")
@Produces(MediaType.APPLICATION_JSON)
public class TagsResource {

    @Inject
    TagRepository tagRepository;

    @GET
    public TagsResponse tags() {
        return new TagsResponse(tagRepository.listAll().stream().map(Tag::getName).sorted().toList());
    }
}
