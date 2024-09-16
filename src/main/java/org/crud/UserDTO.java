package org.crud;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

public class UserDTO {
    private String id;
    @NotEmpty(message = "name cannot be empty field")
    private String name;
    @NotEmpty(message = "email cannot be empty field")
    private String email;
    @Positive(message = "age must be a positive integer")
    private int age;

    @JsonCreator
    public UserDTO(@JsonProperty("id") String id,
                   @JsonProperty("name") String name,
                   @JsonProperty("email") String email,
                   @JsonProperty("age") int age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }

    public void updateNewUser(UserDTO updatedUser) {
        if (updatedUser == null) {
            throw new IllegalArgumentException("Updated user cannot be null");
        }
        if  (updatedUser.getName() != null && !updatedUser.getName().trim().isEmpty()){
            this.name = updatedUser.getName().trim();
        } else {
            throw new IllegalArgumentException("This field cannot be empty or blank!");
        }

        if (updatedUser.getEmail() != null && !updatedUser.getEmail().trim().isEmpty()) {
            this.email = updatedUser.getEmail().trim();
        } else {
            throw new IllegalArgumentException("This field cannot be empty or blank!");
        }

        if (updatedUser.getAge() > 0) {
            this.age = updatedUser.getAge();
        } else {
            throw new IllegalArgumentException("Age must be a positive integer");
        }
    }
}
