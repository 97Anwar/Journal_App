package ye.description.hai.repository;

// Import for MongoDB ObjectId type
import org.bson.types.ObjectId;
// Import for Spring Data MongoDB repository interface
import org.springframework.data.mongodb.repository.MongoRepository;
// Import for the User entity class
import ye.description.hai.entity.User;

/**
 * UserRepo Interface
 * This is a Spring Data MongoDB Repository for handling database operations on User documents.
 * It extends MongoRepository which provides built-in CRUD methods (Create, Read, Update, Delete) automatically.
 * Additionally, it defines a custom method findByUsername() to query users by their unique username.
 * 
 * Generic Parameters:
 * - User: The entity type this repository manages
 * - ObjectId: The type of the primary key/ID field in the entity
 */
// Extends MongoRepository which provides built-in CRUD functionality for User entities in MongoDB
public interface UserRepo extends MongoRepository<User, ObjectId> {
    // Custom query method to find a user by username
    // Spring Data MongoDB automatically generates the implementation of this method
    // It queries the users collection for a user with the specified username
    // @return User object if found, null if no user with that username exists
    User findByUsername(String username); // Query method that finds and returns a User by username
}
