package ye.description.hai.service;

// Import for MongoDB ObjectId type
import org.bson.types.ObjectId;
// Import for Spring dependency injection support
import org.springframework.beans.factory.annotation.Autowired;
// Import for Spring security configuration (not currently used but imported)
import org.springframework.boot.autoconfigure.security.SecurityProperties;
// Import for Spring @Component annotation (marks this as a Spring-managed bean)
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
// Import for the User entity class
import ye.description.hai.entity.User;
// Import for the UserRepo repository interface
import ye.description.hai.repository.UserRepo;
// Import for List collection
import java.util.Arrays;
import java.util.List;
// Import for Optional wrapper
import java.util.Optional;

/**
 * UserService Class
 * This is the business logic layer (Service) for User operations.
 * It acts as a middleman between the controller and repository, handling all business logic for users.
 * This service provides methods for CRUD operations on users and custom queries like finding users by username.
 * The service encapsulates the database access logic and provides a clean interface for controllers to use.
 */
// @Component annotation marks this class as a Spring-managed component/bean
// This makes Spring automatically instantiate and manage this service
@Component // Spring will create and manage an instance of this service as a bean
public class UserService {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder() ;

    // @Autowired annotation tells Spring to inject an instance of UserRepo here (field injection)
    @Autowired // Field injection - Spring automatically provides UserRepo instance
    private UserRepo userrepo; // Dependency injection of the repository layer for database access (note: typo in name 'userrepo' instead of 'userRepo')

    /**
     * Save a User
     * This method saves a new user or updates an existing user in the database
     * @param user The User object to be saved
     */
    // Method to save or update a user in the database
    public void saveUser(User user){ // Takes a User object as parameter
        userrepo.save(user); // Calls the repository's save method which performs insert/update in MongoDB
    }
    public void savenewUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER"));
        userrepo.save(user);
    }

    /**
     * Get all Users
     * This method retrieves all users from the database
     * @return List of all User objects
     */
    // Method to retrieve all users from the database
    public List<User> getAll(){ // Returns a list of all users
        return userrepo.findAll(); // Calls the repository's findAll method to fetch all user documents from MongoDB
    }

    /**
     * Get a User by ID
     * This method retrieves a specific user by their unique ObjectId
     * @param id The ObjectId of the user to retrieve
     * @return Optional containing the User if found, or empty Optional if not found
     */
    // Method to retrieve a user by their ID
    public Optional<User> getById(ObjectId id){ // Takes an ObjectId parameter
        return userrepo.findById(id); // Calls repository's findById method and returns Optional result
    }

    /**
     * Delete a User by ID
     * This method deletes a user from the database
     * @param id The ObjectId of the user to delete
     */
    // Method to delete a user by their ID
    public void deleteById(ObjectId id){ // Takes an ObjectId parameter of the user to delete
        userrepo.deleteById(id); // Calls the repository's deleteById method to remove the user document from MongoDB
    }

    /**
     * Find a User by Username
     * This method retrieves a specific user by their username
     * @param username The username of the user to find
     * @return User object if found, null if no user with that username exists
     */
    // Method to retrieve a user by their username
    public  User findByUsername(String username){ // Takes a username string parameter
        return userrepo.findByUsername(username); // Calls the repository's custom findByUsername method to query by username
    }

}
