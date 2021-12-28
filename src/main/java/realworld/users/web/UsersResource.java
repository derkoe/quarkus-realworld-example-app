package realworld.users.web;

import realworld.users.UserRegistration;
import realworld.users.UserService;

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
    public Response createUser(@Valid UserRequest<UserRegistration> request) {
        return Response.ok(new UserResponse(userService.createUser(request.user))).status(Response.Status.CREATED).build();
    }

    @POST
    @Path("/login")
    public Response login(@Valid UserRequest<UserLogin> request) {
        return Response.ok(new UserResponse(userService.login(request.user))).build();
    }
}

