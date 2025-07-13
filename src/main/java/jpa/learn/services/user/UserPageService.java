package jpa.learn.services.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jpa.learn.beans.User;
import jpa.learn.repos.user.UserPageRepository;

@Service
public class UserPageService {
	
	@Autowired
	private UserPageRepository pageRepository;
	
	
    /**
     * Retrieves all users with pagination.
     * @param pageable Pagination information (page number, size, sort).
     * @return A Page of User objects.
     */
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
    public Page<User> getAllUsers(Pageable pageable) {
        // JpaRepository's findAll(Pageable) is sufficient for this.
        // If you had complex custom logic, you might use a custom query from the repository.
        return pageRepository.findAll(pageable);
    }

    /**
     * Retrieves users by city with pagination, using the named query.
     * @param city The city to search for.
     * @param pageable Pagination information.
     * @return A Page of User objects.
     */
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
    public Page<User> getUsersByCity(String city, Pageable pageable) {
        
    	return pageRepository.findUserByCity(city, pageable);
    }

    /**
     * Retrieves users by zipcode with pagination.
     * @param zipcode The zipcode to search for.
     * @param pageable Pagination information.
     * @return A Page of User objects.
     */
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
    public Page<User> getUsersByZipcode(Integer zipcode, Pageable pageable) {
        
    	return pageRepository.findByAddressZipcode(zipcode, pageable);
    }
	
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public List<User> getTopUsers(int limit) {
		
	    // Create a Pageable object just for the limit, no specific page number needed
	    Pageable topLimit = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "firstName"));
	    return pageRepository.findTopUsers(topLimit);
	}
}
