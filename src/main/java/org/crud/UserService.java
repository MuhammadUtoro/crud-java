package org.crud;

//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Path;
//import java.nio.file.Paths;
import java.util.*;
import org.bson.types.ObjectId; // Import ObjectId
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;

// import io.vertx.mutiny.ext.auth.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
//import org.w3c.dom.stylesheets.LinkStyle;

@ApplicationScoped
public class UserService {

//    private Map<String, UserDTO> users = new HashMap<>();
//
//    //Need to find a way to dynamically adjust the path
//    private static final Path FILE_PATH = Paths.get(System.getProperty("user.dir"), "src", "main", "java", "org", "crud", "users.json");
//
//    public UserService() {
//        loadUsersFromFile();  // Load users from file when starting the service
//    }

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
//        Collection<UserDTO> allUsers = users.values();
        List<UserEntity> allUsers = UserEntity.listAll();
//        System.out.println("Returning " + allUsers.size() + " users.");  // Debugging line
        if (allUsers.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("No users are found! Please add one!").build();
        }
        return Response.ok(allUsers).build();
    }

    // Add a new user
    public Response addUser(UserDTO userDTO) {
        if (userDTO == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("User data cannot be empty").build();
        }
        if (userDTO.getName() == null || userDTO.getName().trim().isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("Name field cannot be empty or blank!").build();
        }
        if (userDTO.getEmail() == null || userDTO.getEmail().trim().isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("Email field cannot be empty or blank!").build();
        }
        if (userDTO.getAge() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Age must be a positive integer").build();
        }

        // Converting DTO to entity
        UserEntity userEntity = new UserEntity();
        userEntity.setName(userDTO.getName().trim());
        userEntity.setEmail(userDTO.getEmail().trim());
        userEntity.setAge(userDTO.getAge());

        // Saving to mongoDB
        userEntity.persist();
        return Response.status(Response.Status.CREATED).entity("User added!").build();
//        String id = UUID.randomUUID().toString();
//        userDTO.setId(id);
//        users.put(id, userDTO);
//        saveUsersToFile();
    }

//    private void saveUsersToFile() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            objectMapper.writeValue(new File(String.valueOf(FILE_PATH)), users);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    // Loading all users at start
//    private void loadUsersFromFile() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            File file = new File(String.valueOf(FILE_PATH));
//            if (file.exists()) {  // Check if the file exists
//                users = objectMapper.readValue(file, new TypeReference<Map<String, UserDTO>>() {});
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    // Get user by ID
    public Response getSingleUser(String id) {
        ObjectId objectId;
        System.out.println("Received ID: " + id); // Log received ID

        try {
            objectId = new ObjectId(id);
        } catch(IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid ObjectId format!").build();
        }

        UserEntity userEntity = UserEntity.findById(objectId);
//        UserDTO user = users.get(id);
        if(userEntity == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found!").build();
        }
        return Response.ok(userEntity).build();
    }

    // Update User by ID
    public Response updateUser(String id, UserDTO updatedUser) {
        ObjectId objectId;

        try {
            objectId = new ObjectId(id);
        } catch(IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid ObjectId format!").build();
        } 

        UserEntity userEntity = UserEntity.findById(objectId);
        if (userEntity == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found!").build();
        }

        UserDTO updatedUserDTO = new UserDTO(userEntity.id.toString(), userEntity.getName().toString(), userEntity.getEmail(), userEntity.getAge());

        try {
            updatedUserDTO.updateNewUser(updatedUser);
    
            userEntity.setName(updatedUserDTO.getName());
            userEntity.setEmail(updatedUserDTO.getEmail());
            userEntity.setAge(updatedUserDTO.getAge());
    
            userEntity.update();
            return Response.ok("User updated successfully").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }

        // if (updatedUser.getName() != null && !updatedUser.getName().trim().isEmpty()) {
        //     userEntity.setName(updatedUser.getName().trim());
        // }  else if (updatedUser.getName() == null) {
        //     return Response.status(Response.Status.BAD_REQUEST).entity("Name field cannot be empty").build();
        // }

        // if (updatedUser.getEmail() != null && !updatedUser.getEmail().trim().isEmpty()) {
        //     userEntity.setEmail(updatedUser.getEmail().trim());
        // }  else if (updatedUser.getEmail() == null) {
        //     return Response.status(Response.Status.BAD_REQUEST).entity("Email field cannot be empty").build();
        // }

        // if (updatedUser.getAge() > 0) {
        //     userEntity.setAge(updatedUser.getAge());
        // } else if (updatedUser.getAge() <= 0) {
        //     return Response.status(Response.Status.BAD_REQUEST).entity("Age must be a positive integer!").build();
        // }

//         try {
//             userEntity.persist();
// //            user.updateNewUser(updatedUser);
// //            users.put(id, user);
// //            saveUsersToFile();
//             return Response.ok("User has been updated").build();
//         } catch (IllegalArgumentException e) {
//             return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
//         } catch (Exception e) {
//             return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while updating the user").build();
//         }
    }

    // Delete User by ID
    public Response deleteUser(String id) {
        ObjectId objectId;

        objectId = new ObjectId(id);

        UserEntity userEntity = UserEntity.findById(objectId);
        if (userEntity == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found!").build();
        }
        userEntity.delete();
//        users.remove(id, user);
//        saveUsersToFile();
        return Response.ok("User has been deleted!").build();
    }
}
