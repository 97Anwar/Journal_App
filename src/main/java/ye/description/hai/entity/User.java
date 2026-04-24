package ye.description.hai.entity;
import lombok.Data;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * User Entity
 * This class represents a User document in the MongoDB database.
 * It contains fields for user identification (id, username, password) and maintains a reference to all journal entries belonging to this user.
 * Each instance of this class maps to a document in the 'users' MongoDB collection.
 */
// @Document annotation maps this class to MongoDB collection named 'users'
// This means each User instance becomes a document (record) in the users collection
@Document(collection = "users") // Maps this entity to the 'users' MongoDB collection
// @Data annotation from Lombok generates getters, setters, toString, equals, hashCode, and constructor automatically at compile time
@Data // Lomboks @Data includes @Getter, @Setter and other useful methods
public class User {
    
    @Id
    private ObjectId id; // MongoDB automatically generates a unique ObjectId for each new user
    
    // @Indexed(unique = true) annotation creates a database index on this field and enforces uniqueness
    // Note: For unique indexing to work, you must set spring.data.mongodb.auto-index-creation=true in application.properties
    @Indexed(unique = true)
    // @NonNull annotation indicates this field is mandatory and cannot be null
    @NonNull // This field is required and Spring will enforce that it cannot be null
    private String username;
    
    @NonNull // This field is required and Spring will enforce that it cannot be null
    private String password;
    
    // @DBRef annotation creates references to JournalEntry documents in MongoDB
    // This establishes a relationship where users can have multiple journal entries
    @DBRef // This creates database references and stores the ObjectIds of all journal entries that belong to this user
    private List<JournalEntry> journalEntries = new ArrayList<>(); // List to store all journal entries created by this user; initialized as empty ArrayList
    private List<String> roles;
}
