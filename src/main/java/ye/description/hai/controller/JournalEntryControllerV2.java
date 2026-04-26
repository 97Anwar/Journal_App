package ye.description.hai.controller;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ye.description.hai.entity.JournalEntry;
import ye.description.hai.entity.User;
import ye.description.hai.service.JournalEntryService;
import ye.description.hai.service.UserService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * JournalEntryControllerV2 (Version 2 Controller)
 * This is the main REST controller for handling all Journal Entry API endpoints.
 * It provides CRUD operations (Create, Read, Update, Delete) for journal entries.
 * This controller works with MongoDB for data persistence and uses JournalEntryService for business logic.
 */
@RestController // Marks this class as a REST controller that handles HTTP requests and returns JSON responses
@RequestMapping("/journal")
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
    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry){ // Receives journal entry from request body
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
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
    @GetMapping // Handles HTTP GET requests to /journal
    public ResponseEntity<?> getAllJournalEntriesOfUser(){ // Method that fetches all entries
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUsername(userName);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(myId)).collect(Collectors.toList());
        if (!collect.isEmpty()){
            Optional<JournalEntry> journalEntry = journalEntryService.getById(myId); // Calls service to find entry by ID, returns Optional
            if (journalEntry.isPresent()){ // Checks if the entry exists
                return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK); // Returns the entry with 200 OK status
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Returns 404 Not Found if entry doesn't exist
    }

    /**
     * Delete a Journal Entry by ID
     * This method deletes a journal entry from the database
     * @param myId The ObjectId of the journal entry to delete
     * @return ResponseEntity with NO_CONTENT status (204)
     */
    @DeleteMapping("id/{myId}") // Handles DELETE requests to /journal/id/{myId}
    public ResponseEntity<?> deleteById(@PathVariable ObjectId myId){ // Extracts myId from the URL path
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        boolean removed = journalEntryService.deleteById(myId, userName);// Calls service to delete the entry from database
        if (removed){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Returns 204 No Content indicating successful deletion
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Update a Journal Entry by ID
     * This method updates an existing journal entry with new data. It only updates fields that are provided and not empty.
     * @param myId The ObjectId of the journal entry to update
     * @param newEntry The new data containing updated title and/or content
     * @return ResponseEntity with the updated entry and OK status if found, or NOT_FOUND status if entry doesn't exist
     */

    @PutMapping("/id/{myId}") // Handles PUT requests to /journal/id/{myId} for updating
    public ResponseEntity<JournalEntry> updateById(
            @PathVariable ObjectId myId,
            @RequestBody JournalEntry newEntry) { // Extracts ID from path and receives new data from request body
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUsername(userName);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(myId)).collect(Collectors.toList());
        if (!collect.isEmpty()){
            Optional<JournalEntry> journalEntry = journalEntryService.getById(myId); // Calls service to find entry by ID, returns Optional
            if (journalEntry.isPresent()){ // Checks if the entry exists
                JournalEntry old = journalEntry.get();
                // Updates title only if the new title is not null and not empty, otherwise keeps the old title
                old.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().isEmpty() ? newEntry.getTitle() : old.getTitle());
                // Updates content only if the new content is not null and not empty, otherwise keeps the old content
                old.setContent(newEntry.getContent() != null && !newEntry.getContent().isEmpty() ? newEntry.getContent() : old.getContent());
                journalEntryService.saveEntry(old); // Saves the updated entry to the database
                return new ResponseEntity<>(old, HttpStatus.OK); // Returns the updated entry with 200 OK status

            }
        }
        if (!collect.isEmpty()) {
            JournalEntry old = journalEntryService.getById(myId).orElse(null); // Fetches the existing entry by ID, returns null if not found
            if (old != null) { // Checks if the entry exists
                }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Returns 404 Not Found if entry doesn't exist
    }
}
