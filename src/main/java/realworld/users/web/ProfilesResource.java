package realworld.users.web;

import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import realworld.security.UserHelper;
import realworld.users.ProfileData;
import realworld.users.UserService;

@Path("/profiles")
@Produces(MediaType.APPLICATION_JSON)
public class ProfilesResource {
  @Inject
  UserService userService;

  @GET
  @Path("{username}")
  @RolesAllowed("User")
  public Map<String, ProfileData> userProfile(@PathParam("username") String username, @Context SecurityContext sec) {
    return userService.getUserProfile(UserHelper.getUserId(sec), username).map(u -> Map.of("profile", u)).orElseThrow(NotFoundException::new);
  }

  @POST
  @Path("{username}/follow")
  @RolesAllowed("User")
  public Map<String, ProfileData> follow(@PathParam("username") String username, @Context SecurityContext sec) {
    return Map.of("profile", userService.addFollower(UserHelper.getUserId(sec), username));
  }

  @DELETE
  @Path("{username}/follow")
  @RolesAllowed("User")
  public Map<String, ProfileData> unfollow(@PathParam("username") String username, @Context SecurityContext sec) {
    return Map.of("profile", userService.removeFollower(UserHelper.getUserId(sec), username));
  }
}
