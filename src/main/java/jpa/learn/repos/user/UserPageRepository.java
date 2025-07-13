package jpa.learn.repos.user;

import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import jpa.learn.beans.User;

//JpaRepository to get pagination features out-of-the-box
public interface UserPageRepository extends JpaRepository<User, Integer> {

    /* --- Paged Methods ---	*/

	/**
	 * This method will use the named query "findUserByCity" defined in the User entity.
	 * Spring Data JPA automatically maps method names like findBy<Property> to named queries if they exist.
	 * 
	 * Lock- Locking DB row until read complete
	 * 
	 * @param city, search user based on their city.
	 * @param pageable Contains pagination and sorting information.
	 * @return A Page of User entities by the specified city.
	 */
	@Lock(LockModeType.PESSIMISTIC_READ)
	@Query(name="findUserByCity")
    Page<User> findUserByCity(String city, Pageable pageable);

    /**
     * Streams all Users with pagination and sorting applied.
     * The sorting and limiting is handled by the Pageable object.
     *
     * Note: JpaRepository's findAll(Pageable pageable) already provides this
     * 
     * @param pageable Contains pagination (page number, page size) and sorting information.
     * @return A Stream of Article entities.
     */
    @Query("SELECT u FROM User u") // Explicit query for clarity, though Spring Data JPA could derive it
    Page<User> findAllUsers(Pageable pageable);

    /**
     * Paged User by a specific zipcode with pagination and sorting applied.
     *
     * @param zipcode, search users based on their zipcode
     * @param pageable Contains pagination and sorting information.
     * @return A Page of User entities by the specified zipcode.
     */
    Page<User> findByAddressZipcode(Integer zipcode, Pageable pageable);

    // Method to find the top N [Limit] users, ordered by firstName
    List<User> findTop10ByOrderByFirstNameDesc();
    
    @Query("SELECT u FROM User u ORDER BY u.firstName DESC")
    List<User> findTopUsers(Pageable pageable);
}
