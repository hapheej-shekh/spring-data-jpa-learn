package jpa.learn.repos.user;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import jpa.learn.beans.User;
import jpa.learn.beans.extra.UserDto;
import jpa.learn.beans.extra.UserInfo1;
import jpa.learn.beans.extra.UserInfo2;

public interface ProjectionRepository extends Repository<User, Integer> {
	
	/* These 2 will not work since User Entity don't have-
	 * OpenProjection & ClosedProjection fields 
	List<UserInfo1> findUserInfoByClosedProjection();
	List<UserInfo2> findUserInfoByOpenProjection(); */
	
	// Interface based projection
	List<UserInfo1> findUserInfo1By();
	List<UserInfo2> findUserInfo2By();
	
	/* Can also use dummy fields for Open & Closed Projections, below methods work same as-
	 * List<UserInfo1> findUserInfo1By(); & List<UserInfo2> findUserInfo2By(); 
	 * Do not need to provide 'name' parameter */
	
	List<UserInfo1> findUserInfoByFirstName(String name);
	List<UserInfo2> findUserInfoByLastName(String name);
	
	/* Class based projection needs Query to convert User to UserDto 
	 * In-order to map/convert few fields from User to UserDto */
	@Query("SELECT new jpa.learn.beans.extra.UserDto(u.firstName, u.lastName, a.city, a.zipcode) " +
		       "FROM User u JOIN u.address a")
	List<UserDto> findUserDtoBy();
	
	
	/*	Dynamic Projection of User	*/
	<T> List<T> findByFirstNameAndAddress_City(String firstName, String city, Class<T> type);
	<T> List<T> findAllBy(Class<T> type);
}
