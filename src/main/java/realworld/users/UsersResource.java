package realworld.users;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/users")
public class UsersResource {

    @Inject
    UserService userService;

    @POST
    public Response createUser(@Valid UserRegistration registration) {
        return Response.ok(userService.createUser(registration)).status(Response.Status.CREATED).build();
    }
}

