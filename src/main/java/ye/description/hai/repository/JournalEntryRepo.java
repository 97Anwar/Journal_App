package ye.description.hai.repository;

// Import for MongoDB ObjectId type
import org.bson.types.ObjectId;
// Import for Spring Data MongoDB repository interface
import org.springframework.data.mongodb.repository.MongoRepository;
// Import for the JournalEntry entity class
import ye.description.hai.entity.JournalEntry;

/**
 * JournalEntryRepo Interface
 * This is a Spring Data MongoDB Repository for handling database operations on JournalEntry documents.
 * It extends MongoRepository which provides built-in CRUD methods (Create, Read, Update, Delete) automatically.
 * By extending MongoRepository, this interface automatically gets methods like save(), findAll(), findById(), deleteById(), etc.
 * 
 * Generic Parameters:
 * - JournalEntry: The entity type this repository manages
 * - ObjectId: The type of the primary key/ID field in the entity
 */
// Extends MongoRepository which provides built-in CRUD functionality for JournalEntry entities in MongoDB
// MongoRepository handles all common database operations automatically including save, findAll, findById, deleteById
public interface JournalEntryRepo extends MongoRepository<JournalEntry, ObjectId> {
    // MongoRepository provides the following methods automatically:
    // - save(JournalEntry): Insert or update a journal entry
    // - findAll(): Get all journal entries
    // - findById(ObjectId): Get a journal entry by ID
    // - deleteById(ObjectId): Delete a journal entry by ID
    // - delete(JournalEntry): Delete a specific journal entry
    // - count(): Count total number of entries
    // - existsById(ObjectId): Check if entry exists by ID
}
