package jpa.learn.api.user;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jpa.learn.beans.User;
import jpa.learn.beans.extra.PageDetail;
import jpa.learn.services.user.UserSpecificationService;

/* Specification based projection [Specification internally usage Criteria] */
@RestController
@RequestMapping("/api/users")
public class UserSpecificationApi {

	@Autowired
    private UserSpecificationService userService;


    /* --- Criteria Based Search ---
     * 
     * Get -> /api/users/by-criteria?city=Delhi
     * Others can be null since optional while building query
     */
    @GetMapping("/by-criteria")
    public List<User> getByCriteria(@RequestParam String city) {
        
    	return userService.searchUsersCriteria(null, city, null, null, null);
    }
    
    
	/*	--- Pageable Api --- 
	 * 
	 * Get -> /api/users?city=CityName
	 */
    @GetMapping(params="city")
    public Page<User> searchCustomers(@RequestParam(required=false) String city, 
    		@RequestBody PageDetail detail) {
    	
        return userService.searchUsers(city, detail);
    }
    
    // Get -> /api/users/by-city?city=Delhi
    @GetMapping("/by-city")
    public List<User> getByCity(@RequestParam String city) {
    	
        return userService.findByCity(city);
    }

    // Get -> /api/users/by-join-date?startDate=01-01-2023&endDate=12-31-2023
    @GetMapping("/by-join-date")
    public List<User> getByJoinDateBetween(
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Date endDate) {
    	
        return userService.findByJoinDateBetween(startDate, endDate);
    }

    // Get -> /api/users/by-old-user?isOldUser=true
    @GetMapping("/by-old-user")
    public List<User> getByOldUserStatus(@RequestParam Boolean isOldUser) {
    	
        return userService.findByOldUserStatus(isOldUser);
    }

    // Get -> /api/users/by-name?name=John
    @GetMapping("/by-name")
    public List<User> getByName(@RequestParam String firstName) {
    	
        return userService.findByfirstName(firstName);
    }

    
	/* --- Compound Method for All In One ---
	 *  
	 * GET /users/search
	 * GET /users/search?city=Delhi&isOldCustomer=true
	 * GET /users/search?name=John&isOldCustomer=false
	 * GET /users/search?startDate=07-012023&endDate=07-01-2024
	 */
    @GetMapping("/search")
    public List<User> searchCustomers(
            @RequestParam(required=false) String name,
            @RequestParam(required=false) String city,
            @RequestParam(required=false) Boolean isOldCustomer,
            @RequestParam(required=false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date startDate,
            @RequestParam(required=false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date endDate
    ) {
    	
        return userService.searchUsers(name, city, isOldCustomer, startDate, endDate);
    }    
}
