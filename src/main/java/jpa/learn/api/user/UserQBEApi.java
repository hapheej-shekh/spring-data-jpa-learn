package jpa.learn.api.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jpa.learn.beans.User;
import jpa.learn.beans.extra.PageDetail;
import jpa.learn.services.user.UserService;

/**
 *	This class demonstrate Query by Example- Dynamic Query Mechanism
 */
@RestController
@RequestMapping("/api/qbe")
public class UserQBEApi {

	@Autowired
	private UserService userService;
	
	
    @GetMapping(params="firstName")
    public ResponseEntity<?> getUsersByFirstname(@RequestParam String firstName) {
    	
    	List<User> users = userService.getUserByFirstname(firstName);
    	
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(params="city")
    public ResponseEntity<?> getUsersByCity(@RequestParam String city) {
    	
    	List<User> users = userService.getUserByCity(city);
    	
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    
    @GetMapping(params={"firstName", "city"})
    public ResponseEntity<?> getUsersByFirstNameAndCity(@RequestParam String firstName,
    		@RequestParam String city) {
    	
    	List<User> users = userService.findUserByFirstNameAndCity(firstName, city);
    	
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> getUsersByPage(@RequestBody PageDetail detail) {
    	
    	Page<User> users = userService.findUserByPage(detail);
    	
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
