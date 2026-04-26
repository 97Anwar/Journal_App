package ye.description.hai.service;

// Import for MongoDB ObjectId type
import org.bson.types.ObjectId;
// Import for Spring dependency injection support
import org.springframework.beans.factory.annotation.Autowired;
// Import for Spring @Component annotation (marks this as a Spring-managed bean)
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ye.description.hai.entity.JournalEntry;
import ye.description.hai.entity.User;
import ye.description.hai.repository.JournalEntryRepo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * JournalEntryService Class
 * This is the business logic layer (Service) for JournalEntry operations.
 * It acts as a middleman between the controller and repository, handling all business logic for journal entries.
 * This service provides methods for CRUD operations on journal entries using the JournalEntryRepo repository.
 * The service encapsulates the database access logic and provides a clean interface for controllers to use.
 */
// @Component annotation marks this class as a Spring-managed component/bean
// This makes Spring automatically instantiate and manage this service
@Component // Spring will create and manage an instance of this service as a bean
public class JournalEntryService {

    // @Autowired annotation tells Spring to inject an instance of JournalEntryRepo here
    @Autowired // Spring dependency injection - automatically provides JournalEntryRepo instance
    private JournalEntryRepo journalEntryRepo; // Dependency injection of the repository layer for database access

    @Autowired
    private UserService userService;
    /**
     * Save a Journal Entry
     * This method saves a new journal entry or updates an existing one in the database
     *
     * @param journalEntry The JournalEntry object to be saved
     * @param user
     */
    // Method to save or update a journal entry in the database
    @Transactional
    public void saveEntry(JournalEntry journalEntry, String userName){ // Takes a JournalEntry as parameter
        User user = userService.findByUsername(userName);
        journalEntry.setDate(LocalDateTime.now()); // Sets the current date and time when the entry is created
        JournalEntry saved = journalEntryRepo.save(journalEntry);// Calls the repository's save method which performs insert/update in MongoDB
        user.getJournalEntries().add(saved);
        userService.saveUser(user);
    }

    public void saveEntry(JournalEntry journalEntry){ // Takes a JournalEntry as parameter
        journalEntry.setDate(LocalDateTime.now()); // Sets the current date and time when the entry is created
        journalEntryRepo.save(journalEntry);// Calls the repository's save method which performs insert/update in MongoDB
    }

    /**
     * Get all Journal Entries
     * This method retrieves all journal entries from the database
     * @return List of all JournalEntry objects
     */
    // Method to retrieve all journal entries from the database
    public List<JournalEntry> getAll(){ // Returns a list of all journal entries
        return journalEntryRepo.findAll(); // Calls the repository's findAll method to fetch all documents from MongoDB
    }

    /**
     * Get a Journal Entry by ID
     * This method retrieves a specific journal entry by its unique ID
     * @param id The ObjectId of the journal entry to retrieve
     * @return Optional containing the JournalEntry if found, or empty Optional if not found
     */
    // Method to retrieve a journal entry by its ID
    public Optional<JournalEntry> getById(ObjectId id){ // Takes an ObjectId parameter
        return journalEntryRepo.findById(id); // Calls repository's findById method and returns Optional result
    }

    /**
     * Delete a Journal Entry by ID
     * This method deletes a journal entry from the database
     *
     * @param id       The ObjectId of the journal entry to delete
     */
    // Method to delete a journal entry by its ID
    @Transactional
    public boolean deleteById(ObjectId id, String userName){ // Takes an ObjectId parameter of the entry to delete
        boolean removed = false;
        try {
            User user = userService.findByUsername(userName);
            removed = user.getJournalEntries().removeIf(x -> x.getId().equals(id));
            if (removed){
                userService.saveUser(user);
                journalEntryRepo.deleteById(id); // Calls the repository's deleteById method to remove the document from MongoDB
            }
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("An error occured while deleting the entry.", e);
        }
        return removed;
    }
}
