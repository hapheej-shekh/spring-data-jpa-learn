package jpa.learn.services.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jpa.learn.beans.Address;
import jpa.learn.beans.User;
import jpa.learn.beans.extra.PageDetail;
import jpa.learn.repos.user.UserExampleRepository;
import jpa.learn.repos.user.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private UserExampleRepository exampleRepo; //JpaRepository support
	
	
    /**
     * Saves a new user or updates an existing one.
     * @param user The user object to save.
     * @return The saved user object.
     */
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
    public User saveUser(User user) {
    	
		user.updateJoiningDate();
		
        return userRepo.save(user);
    }
	
    /**
     * Retrieves a user by their ID.
     * @param id The ID of the user.
     * @return An Optional containing the User if found, empty otherwise.
     */
	@Transactional(readOnly=true)
    public Optional<User> getUserById(Integer id) {
    	
        return userRepo.findById(id);
    }
	
    @Transactional(readOnly=true)
    public Iterable<User> getUsers() {
    	
        return userRepo.findAll();
    }

    /**
     * Deletes a user by their ID.
     * @param id The ID of the user to delete.
     */
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
    public void deleteUser(Integer id) {
    	
		userRepo.deleteById(id);
    }
	
	@Transactional(readOnly=true)
    public List<User> getUserByFirstname(String firstname) {
    	
		User user = new User();
		user.setFirstName(firstname);

		Example<User> example = Example.of(user);
		
		return exampleRepo.findAll(example);
    }

	public List<User> getUserByCity(String city) {

		Address adr = new Address();
		adr.setCity(city);
		
		ExampleMatcher matcher = ExampleMatcher.matching()
		        .withIgnorePaths("firstName", "lastName")  // If you want to ignore some fields
		        .withMatcher("address.city", ExampleMatcher.GenericPropertyMatchers.exact());

		Example<User> example = Example.of(new User(adr), matcher);
		
		return exampleRepo.findAll(example);
	}
	
	public List<User> findUserByFirstNameAndCity(String firstName, String city) {
		
		Address addr = new Address();
	    addr.setCity(city);
	    
		User user = new User(addr);
	    user.setFirstName(firstName);	    
	    
	    Sort sort = Sort.by(Sort.Direction.ASC, "firstName");

	    ExampleMatcher matcher = ExampleMatcher.matchingAll()
	            .withIgnoreNullValues()
	            .withIgnoreCase();

	    Example<User> example = Example.of(user, matcher);
	    
	    return exampleRepo.findAll(example, sort);
	}
	
	public Page<User> findUserByPage(PageDetail detail) {
		
		String dir = "Asc";
		
		if(detail.getSortDir()!=null && !detail.getSortDir().isBlank())
			dir = detail.getSortDir().toUpperCase();
		
		Example<User> example = Example.of(new User());
		
		Sort.Direction direction = Sort.Direction.fromString(dir); // handles case-insensitive input
		
		Pageable pageable = PageRequest.of(detail.getPage(), detail.getSize(),
				Sort.by(direction, detail.getSortBy()));
		
		Page<User> page = exampleRepo.findAll(example, pageable);
		
		return page;
	}
}
