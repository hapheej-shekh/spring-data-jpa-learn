package jpa.learn.api.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jpa.learn.beans.User;
import jpa.learn.services.user.UserPageService;

/**
 * Pageable/Page having Sort & Limit inbuilt
 * If we define separately then combination will be error prone
 */
@RestController
@RequestMapping("/api/users/page")
public class UserPageApi {

	@Autowired
	private UserPageService userService;


    /**
     * Retrieves all users with pagination and optional sorting.
     * GET /api/users?page=0&size=10&sort=firstName,asc
     * GET /api/users?page=X&size=Y&sort=Z,desc
     * @param page The page number (default 0).
     * @param size The number of items per page (default 10).
     * @param sort The sort criteria (e.g., "firstName,asc" or "lastName,desc").
     * @return A Page of User objects.
     */
    @GetMapping(params={"page", "size", "sort"})
    public ResponseEntity<Page<User>> getAllUsers(
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="10") int size,
            @RequestParam(defaultValue="id,asc") String[] sort) {

        List<Sort.Order> orders = new ArrayList<>();

        // Add the primary sort criterion
        Sort.Direction primaryDirection = Sort.Direction.fromString(sort[1]);
        orders.add(new Sort.Order(primaryDirection, sort[0]));

        /**
         * In primary sort criterion is not unique it may result duplicate values over pages,
         * Add a secondary sort criterion by ID to ensure stable pagination.
         * This prevents records with the same primary sort value from "shifting" between pages.
         * Only add if the primary sort isn't already by 'id'.
         */
        if (!sort[0].equalsIgnoreCase("id"))
            orders.add(new Sort.Order(Sort.Direction.ASC, "id")); // Always sort by ID ascending as a tie-breaker

        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
        Page<User> users = userService.getAllUsers(pageable);

        return ResponseEntity.ok(users);
    }

    /**
     * Retrieves users by city with pagination.
     * GET /api/users/city/{city}?page=0&size=10
     * @param city The city to search for.
     * @param page The page number.
     * @param size The number of items per page.
     * @return A Page of User objects.
     */
    @GetMapping("/city/{city}")
    public ResponseEntity<Page<User>> getUsersByCity(
            @PathVariable String city,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
    	Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userService.getUsersByCity(city, pageable);
        
        return ResponseEntity.ok(users);
    }

    /**
     * Retrieves users by zipcode with pagination.
     * GET /api/users/zipcode/{zipcode}?page=0&size=10
     * @param zipcode The zipcode to search for.
     * @param page The page number.
     * @param size The number of items per page.
     * @return A Page of User objects.
     */
    @GetMapping("/zipcode/{zipcode}")
    public ResponseEntity<Page<User>> getUsersByZipcode(
            @PathVariable Integer zipcode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
    	Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userService.getUsersByZipcode(zipcode, pageable);
        
        return ResponseEntity.ok(users);
    }
    
    /**
     * GET /api/users/top?limit=5
     * @param limit, number of users needed
     * @return users based on limit or max-record available
     */
    @GetMapping("/top")
    public ResponseEntity<List<User>> getTopUsers(@RequestParam(defaultValue="10") int limit) {
        
    	List<User> users = userService.getTopUsers(limit);
        
    	return ResponseEntity.ok(users);
    }
}
