package realworld.users.web;

import org.eclipse.microprofile.jwt.JsonWebToken;
import realworld.users.UserService;
import realworld.users.UserUpdate;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

@Path("/user")
public class UserResource {

    @Inject
    JsonWebToken jwt;

    @Inject
    UserService userService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("User")
    public UserResponse currentUser() {
        return userService.getUser(UUID.fromString(jwt.getSubject())).map(UserResponse::new).orElse(null);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("User")
    public UserResponse updateUser(UserRequest<UserUpdate> request) {
        return new UserResponse(userService.updateUser(UUID.fromString(jwt.getSubject()), request.user));
    }
}

