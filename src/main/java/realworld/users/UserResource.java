package realworld.users;

import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.UUID;

@Path("/user")
public class UserResource {

    @Inject
    JsonWebToken jwt;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("User")
    public UserWithToken currentUser(@Context SecurityContext ctx) {
        User user = User.findById(UUID.fromString(jwt.getSubject()));
        return user == null ? null : new UserWithToken(user.username, user.email, jwt.getRawToken());
    }
}
