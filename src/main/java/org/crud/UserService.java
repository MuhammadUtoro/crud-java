package org.crud;

import java.io.File;
import java.io.IOException;
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
    private static final String FILE_PATH = "D:\\quarkus-crud\\src\\main\\java\\org\\crud\\users.json";  // File to save data

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
            objectMapper.writeValue(new File(FILE_PATH), users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadUsersFromFile() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File file = new File(FILE_PATH);
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
}
