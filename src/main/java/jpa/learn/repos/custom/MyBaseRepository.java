package jpa.learn.repos.custom;

import java.util.Optional;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

@NoRepositoryBean
public interface MyBaseRepository<T, ID> extends Repository<T, ID> {

	Optional<T> findById(ID id);
	<S extends T> S save(S entity);
	  
	/*	Write methods those are common for all or multiple repositories
	 * 
	 *	interface UserRepository extends MyBaseRepository<User, Long> {
	 * 		User findByEmailAddress(EmailAddress emailAddress);
	 * 		//Optional<T> findById(ID id);	-> available here
	 * 		//<S extends T> S save(S entity);	-> available here
	 *	}	
	 * */
}
