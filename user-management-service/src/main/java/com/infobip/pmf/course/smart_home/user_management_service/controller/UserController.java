package com.infobip.pmf.course.smart_home.user_management_service.controller;

import com.infobip.pmf.course.smart_home.user_management_service.model.User;
import com.infobip.pmf.course.smart_home.user_management_service.model.UserDTO;
import com.infobip.pmf.course.smart_home.user_management_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController 
{
    @Autowired
    private UserService userService;

    // Create a new user / register a user
    @PostMapping("/register")
    public ResponseEntity<User> createUser(@RequestBody User user) 
    {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    // Get all users (without API keys)
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() 
    {
        List<UserDTO> users = userService.getAllUsers();
        if(users.isEmpty()) 
        {
            return ResponseEntity.noContent().build();  // 204 No Content if no users are found
        }
        return ResponseEntity.ok(users);  // 200 OK with user list
    }

    // Get a user by ID,  for ex. UserClient in device ms needs it
    @GetMapping("/{id}") 
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) 
    {
        UserDTO userDTO = userService.getUserById(id);
        if(userDTO != null) 
        {
            return ResponseEntity.ok(userDTO);
        }
        return ResponseEntity.notFound().build();
    }

    // Delete a user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) 
    {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /* --> ambiguity!
    // Get a user by username
    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) 
    {
        UserDTO userDTO = userService.getUserByUsername(username);
        if(userDTO != null) 
        {
            return ResponseEntity.ok(userDTO);
        }
        return ResponseEntity.notFound().build();
    }
    */

    // for api gateway, apy key gateway filter, path is defined in the UserClient feignclinet
    // an API endpoint that validates the API key
    @GetMapping("/validate-api-key")
    public ResponseEntity<Boolean> validateApiKey(@RequestParam("apiKey") String apiKey) 
    {
        boolean isValid = userService.validateApiKey(apiKey);
        return ResponseEntity.ok(isValid);
    }

    /*
     * ResponseEntity allows to control the HTTP status code and headers explicitly. 
     * For instance, you can return 200 OK for success or 404 NOT FOUND if the resource doesn't exist.
     * Direct return (e.g., User) assumes a default 200 OK response without custom headers or status codes, limiting flexibility.
     * By returning ResponseEntity, you gain more control over the response.
    */
}





 /* 
    @GetMapping("/{userId}/with-devices")  // @GetMapping("/users/{userId}/with-devices")
    public User getUserWithDevices(@PathVariable Long userId)
    {
        return userService.getUserWithDevices(userId);
    }
    */

    /* 
    @GetMapping("/{username}")
    public User getUserByUsername(@PathVariable String username) 
    {
        return userService.findUserByUsername(username).orElse(null);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) 
    {
        return userService.findUserByUsername(username)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    */

    

