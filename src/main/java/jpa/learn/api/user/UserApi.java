package jpa.learn.api.user;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jpa.learn.beans.User;
import jpa.learn.services.user.UserService;

@RestController
@RequestMapping("/api/users")
public class UserApi {

	private final UserService userService;
	
	public UserApi(UserService userService) {
		this.userService = userService;
	}
	
	
    /**
     * Creates a new user.
     * POST /api/users
     * @param user The user object from the request body.
     * @return The created user with HTTP status 201 (Created).
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
    	
        User savedUser = userService.saveUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    /**
     * Retrieves a user by ID.
     * GET /api/users/{id}
     * @param id The ID of the user.
     * @return The User object if found, or 404 Not Found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
    	
        Optional<User> user = userService.getUserById(id);
        
        return user.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Retrieves all user
     * GET /api/users
     * @return List of all available User
     */
    @GetMapping
    public ResponseEntity<?> getUsers() {
    	
        Iterable<User> users = userService.getUsers();
        
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * Deletes a user by ID.
     * DELETE /api/users/{id}
     * @param id The ID of the user to delete.
     * @return HTTP status 204 (No Content) if successful.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
    	
        userService.deleteUser(id);
        
        return ResponseEntity.ok("User deleted, id: "+id);
    }
}
