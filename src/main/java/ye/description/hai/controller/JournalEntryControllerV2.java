package ye.description.hai.controller;

// Imports for MongoDB ObjectId type
import org.bson.types.ObjectId;
// Import for Spring dependency injection
import org.springframework.beans.factory.annotation.Autowired;
// Imports for HTTP response status codes
import org.springframework.http.HttpStatus;
// Import for wrapping responses with status codes
import org.springframework.http.ResponseEntity;
// Imports for REST endpoint annotations
import org.springframework.web.bind.annotation.*;
// Import for the JournalEntry entity model
import ye.description.hai.entity.JournalEntry;
// Import for the JournalEntryService business logic layer
import ye.description.hai.entity.User;
import ye.description.hai.service.JournalEntryService;
import ye.description.hai.service.UserService;

// Imports for handling dates and times
import java.time.LocalDateTime;
// Import for List collection
import java.util.List;
// Import for Optional wrapper
import java.util.Optional;

/**
 * JournalEntryControllerV2 (Version 2 Controller)
 * This is the main REST controller for handling all Journal Entry API endpoints.
 * It provides CRUD operations (Create, Read, Update, Delete) for journal entries.
 * This controller works with MongoDB for data persistence and uses JournalEntryService for business logic.
 */
@RestController // Marks this class as a REST controller that handles HTTP requests and returns JSON responses
@RequestMapping("/journal") // Base URL path for all endpoints in this controller is /journal
public class JournalEntryControllerV2 {

    // Injects an instance of JournalEntryService using Spring's dependency injection
    @Autowired // Spring will automatically provide an instance of JournalEntryService
    private JournalEntryService journalEntryService; // Service layer dependency that handles business logic
    @Autowired
    private UserService userService;
    /**
     * Create a new Journal Entry
     * This method handles POST requests to create a new journal entry
     * @param myEntry The journal entry object sent in the request body
     * @return ResponseEntity with the created entry and HTTP status CREATED (201) or BAD_REQUEST (400) on error
     */
    @PostMapping("{userName}")
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry, @PathVariable String userName){ // Receives journal entry from request body
        try {
            journalEntryService.saveEntry(myEntry, userName); // Calls service to save the entry to the database
            return  new ResponseEntity<>(myEntry, HttpStatus.CREATED); // Returns the created entry with 201 status code
        } catch (Exception e) {
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Returns 400 Bad Request status code
        }
    }

    /**
     * Get all Journal Entries
     * This method retrieves all journal entries from the database
     * @return List of all journal entries
     */
    @GetMapping("{userName}") // Handles HTTP GET requests to /journal
    public ResponseEntity<?> getAllJournalEntriesOfUser(@PathVariable String userName){ // Method that fetches all entries
        User user = userService.findByUsername(userName);//found user from db
        List<JournalEntry> all = user.getJournalEntries();
        if (all != null && !all.isEmpty()){
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Get a Journal Entry by ID
     * This method retrieves a specific journal entry by its unique ID
     * @param myId The ObjectId of the journal entry to retrieve
     * @return ResponseEntity with the entry and OK status if found, or NOT_FOUND status if not found
     */
    @GetMapping("id/{myId}") // Handles GET requests to /journal/id/{myId} where myId is a path variable
    public ResponseEntity<JournalEntry> getEntryById(@PathVariable ObjectId myId){ // Extracts myId from the URL path
        Optional<JournalEntry> journalEntry = journalEntryService.getById(myId); // Calls service to find entry by ID, returns Optional
        if (journalEntry.isPresent()){ // Checks if the entry exists
            return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK); // Returns the entry with 200 OK status
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Returns 404 Not Found if entry doesn't exist
    }

    /**
     * Delete a Journal Entry by ID
     * This method deletes a journal entry from the database
     * @param myId The ObjectId of the journal entry to delete
     * @return ResponseEntity with NO_CONTENT status (204)
     */
    @DeleteMapping("id/{userName}/{myId}") // Handles DELETE requests to /journal/id/{myId}
    public ResponseEntity<?> deleteById(@PathVariable ObjectId myId, @PathVariable String userName){ // Extracts myId from the URL path
        journalEntryService.deleteById(myId,userName); // Calls service to delete the entry from database
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Returns 204 No Content indicating successful deletion
    }

    /**
     * Update a Journal Entry by ID
     * This method updates an existing journal entry with new data. It only updates fields that are provided and not empty.
     * @param myId The ObjectId of the journal entry to update
     * @param newEntry The new data containing updated title and/or content
     * @return ResponseEntity with the updated entry and OK status if found, or NOT_FOUND status if entry doesn't exist
     */

    @PutMapping("/id/{userName}/{myId}") // Handles PUT requests to /journal/id/{myId} for updating
    public ResponseEntity<JournalEntry> updateById(
            @PathVariable ObjectId myId,
            @RequestBody JournalEntry newEntry,
            @PathVariable String userName) { // Extracts ID from path and receives new data from request body
        JournalEntry old = journalEntryService.getById(myId).orElse(null); // Fetches the existing entry by ID, returns null if not found
        if( old != null){ // Checks if the entry exists
            // Updates title only if the new title is not null and not empty, otherwise keeps the old title
            old.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().isEmpty() ? newEntry.getTitle() : old.getTitle());
            // Updates content only if the new content is not null and not empty, otherwise keeps the old content
            old.setContent(newEntry.getContent() != null && !newEntry.getContent().isEmpty() ? newEntry.getContent() : old.getContent());
            journalEntryService.saveEntry(old); // Saves the updated entry to the database
            return new ResponseEntity<>(old, HttpStatus.OK); // Returns the updated entry with 200 OK status
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Returns 404 Not Found if entry doesn't exist
    }
}
