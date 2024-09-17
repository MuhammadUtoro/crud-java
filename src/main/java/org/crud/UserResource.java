package org.crud;

// import io.vertx.ext.auth.User;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/hello")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserService userService;

    @GET
    public Response greeting() {
        return userService.greeting();
    }

    @GET
    @Path("all")
    public Response getUsers() {
        return userService.getUsers();
    }

    @POST
    public Response addUser(@Valid UserDTO userDTO) {
        return userService.addUser(userDTO);
    }

    @GET
    @Path("{id}")
    public Response getSingleUser(@PathParam("id") String id) {
        return userService.getSingleUser(id);
    }

    @PUT
    @Path("{id}")
    public Response updateUser(@PathParam("id") String id, UserDTO updatedUser) {
        return userService.updateUser(id, updatedUser);
    }

    @DELETE
    @Path("{id}")
    public Response deleteUser(@PathParam("id") String id) {
        return userService.deleteUser(id);
    }
}
