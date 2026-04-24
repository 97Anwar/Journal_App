package ye.description.hai.entity;

// Import for Lombok @Data annotation (generates getters, setters, toString, equals, hashCode)
import lombok.Data;
// Import for Lombok @Getter annotation (generates getter methods)
import lombok.Getter;
// Import for Lombok @NonNull annotation (marks fields as required)
import lombok.NonNull;
// Import for Lombok @Setter annotation (generates setter methods)
import lombok.Setter;
// Import for MongoDB ObjectId type
import org.bson.types.ObjectId;
// Import for Spring Data MongoDB @Id annotation (marks field as primary key)
import org.springframework.data.annotation.Id;
// Import for Spring Data MongoDB @Document annotation (maps this class to MongoDB collection)
import org.springframework.data.mongodb.core.mapping.Document;

// Import for handling date and time with LocalDateTime
import java.time.LocalDateTime;
// Import for legacy Date class (not currently used in this entity)
import java.util.Date;

/**
 * JournalEntry Entity
 * This class represents a Journal Entry document in the MongoDB database.
 * It contains the structure and mapping for storing journal entries with title, content, and timestamp.
 * Each instance of this class is automatically persisted as a document in the 'journal_entries' MongoDB collection.
 */
// @Getter annotation generates getter methods for all fields at compile time using Lombok
@Getter
// @Setter annotation generates setter methods for all fields at compile time using Lombok
@Setter
// Commented out @Data - this single annotation generates getters, setters, toString, equals, hashCode, and constructor
//@Data
// @Document annotation maps this class to MongoDB collection named 'journal_entries'
// This means each instance of JournalEntry becomes a document (row) in the journal_entries collection
@Document(collection = "journal_entries") // Maps this entity to the 'journal_entries' MongoDB collection
public class JournalEntry {

    // @Id annotation marks this field as the primary key/unique identifier in MongoDB
    @Id // This field will be automatically managed by MongoDB and Spring Data as the document's unique ID
    private ObjectId id; // MongoDB automatically generates a unique ObjectId for each new journal entry
    
    // @NonNull annotation indicates this field is mandatory and cannot be null
    @NonNull // This field is required and Spring Data will enforce non-null constraint
    private String title; // The title of the journal entry
    
    // Content of the journal entry (can be null/empty)
    private String content; // The main body text of the journal entry
    
    // The date and time when the journal entry was created or last modified
    private LocalDateTime date; // Stores the timestamp of when the entry was created using LocalDateTime which includes date and time

}
