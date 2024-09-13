package org.crud;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class UserService {
    private Map<String, UserDTO> users = new HashMap<>();

    // Need to find a way to dynamically adjust the path
    private static final Path FILE_PATH = Paths.get(System.getProperty("user.dir"), "src", "main", "java", "org", "crud", "users.json");

    public UserService() {
        loadUsersFromFile();  // Load users from file when starting the service
    }

    public Response greeting() {
        return Response.ok("Hi swimmers!").build();
    }

    // Test data
    // public UserService() {
    //     String id = UUID.randomUUID().toString();
    //     users.put(id, new UserDTO(id, "Tom Araya", "t.araya@example.ex", 45));
    // }

    // Get all users from users map
    public Response getUsers() {
        Collection<UserDTO> allUsers = users.values();
        System.out.println("Returning " + allUsers.size() + " users.");  // Debugging line
        if (allUsers.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("No users are found! Please add one!").build();
        }
        return Response.ok(allUsers).build();
    }

    // Add a new user
    public Response addUser(UserDTO userDTO) {
        String id = UUID.randomUUID().toString();
        userDTO.setId(id);
        users.put(id, userDTO);
        saveUsersToFile();
        return Response.status(Response.Status.CREATED).entity("User added!").build();
    }

    private void saveUsersToFile() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(String.valueOf(FILE_PATH)), users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Loading all users at start
    private void loadUsersFromFile() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File file = new File(String.valueOf(FILE_PATH));
            if (file.exists()) {  // Check if the file exists
                users = objectMapper.readValue(file, new TypeReference<Map<String, UserDTO>>() {});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Get user by ID
    public Response getSingleUser(String id) {
        UserDTO user = users.get(id);
        if(user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found!").build();
        }
        return Response.ok(user).build();
    }

    // Update Swimmer by ID
    public Response updateUser(String id, UserDTO updatedUser) {
        UserDTO user = users.get(id);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found!").build();

        }

        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        user.setAge(updatedUser.getAge());

        users.put(id, user);
        saveUsersToFile();
        return Response.ok("User has been updated").build();
    }

    // Delete ID by Swimmer
    public Response deleteUser(String id) {
        UserDTO user = users.get(id);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found!").build();
        }
        users.remove(id, user);
        saveUsersToFile();
        return Response.ok("User has been deleted!").build();
    }
}
