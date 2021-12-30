package realworld.users.web;

import realworld.security.UserHelper;
import realworld.users.UserService;
import realworld.users.UserUpdate;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserService userService;

    @GET
    @RolesAllowed("User")
    public UserResponse currentUser(@Context SecurityContext sec) {
        return userService.getUser(UserHelper.getUserId(sec)).map(UserResponse::new).orElse(null);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("User")
    public UserResponse updateUser(UserRequest<UserUpdate> request, @Context SecurityContext sec) {
        return new UserResponse(userService.updateUser(UserHelper.getUserId(sec), request.user));
    }
}
