package ye.description.hai.controller;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ye.description.hai.entity.User;
import ye.description.hai.service.UserService;

import java.util.List;

/**
 * UserController
 * This REST controller handles all user-related API endpoints.
 * It provides CRUD operations for User objects including fetching, creating, and updating users.
 * All requests to /users endpoint are handled by this controller using UserService for business logic.
 */
@RestController // Marks this class as a REST controller that handles HTTP requests and returns JSON responses
@RequestMapping("/user") // Base URL path for all endpoints in this controller is /users
public class UserController {

    // Injects an instance of UserService using Spring's dependency injection
    @Autowired // Spring will automatically provide an instance of UserService
    private UserService userService; // Service layer dependency that handles user business logic

    /**
     * Get all Users
     * This method retrieves all users from the database
     * @return List of all User objects
     */
    @GetMapping // Handles HTTP GET requests to /users
    public List<User> getAll(){ // Method that fetches all users
        return userService.getAll(); // Calls service to retrieve all users from database
    }

    /**
     * Create a new User
     * This method handles POST requests to create and save a new user
     * @param user The user object received from the request body
     * @return void (no response body, just saves the user)
     */


    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username){
        User userIndb = userService.findByUsername(username);
        if (userIndb != null){
            userService.deleteById(userIndb.getId());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Update an existing User
     * This method handles PUT requests to update a user by username
     * @param user The user object with updated data from the request body
     * @return ResponseEntity with NO_CONTENT status (204) to indicate successful update
     */
    @PutMapping() // Handles PUT requests to /users/{username} where username is a path variable
    public ResponseEntity<?> updateUser(@RequestBody User user){ // Receives updated user data and extracts username from URL path
        //         User userInDb = userService.findByUsername(user.getUsername());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User userInDb = userService.findByUsername(username); // Finds the existing user in database by username
        if(userInDb != null){ // Checks if the user exists
            userInDb.setUsername(user.getUsername()); // Updates the username with new value
            userInDb.setPassword(user.getPassword()); // Updates the password with new value
            userService.savenewUser(userInDb); // Saves the updated user back to the database
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Returns 204 No Content status indicating successful update
    }


}
