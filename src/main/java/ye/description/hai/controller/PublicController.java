package ye.description.hai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ye.description.hai.entity.User;
import ye.description.hai.service.UserService;


@RestController // Marks this class as a REST controller that handles HTTP requests
@RequestMapping("/public")
public class PublicController {

    @Autowired
    UserService userService;

    @GetMapping("/health") // Maps GET requests to the /health endpoint
    public String healthCheck(){ // Method that responds to health check requests
        return "OK"; // Returns the string "OK" to indicate the API is running
    }

    @PostMapping("/create-user") // Handles HTTP POST requests to /users
    public void crateUser(@RequestBody User user){ // Receives user data from the request body
        userService.savenewUser(user); // Calls service to save the user to the database
    }

}
