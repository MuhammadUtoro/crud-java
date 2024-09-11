package org.crud;

import jakarta.ws.rs.Produces;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
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

    @Path("all")
    @GET
    public Response getUsers() {
        return userService.getUsers();
    }

    @POST
    public Response addUser(UserDTO userDTO) {
        return userService.addUser(userDTO);
    }

    @Path("{id}")
    @GET
    public Response getSingleUser(@PathParam("id") String id) {
        return userService.getSingleUser(id);
    }
}
