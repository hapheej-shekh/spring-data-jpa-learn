package jpa.learn.services.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jpa.learn.beans.extra.DynamicUserInfo1;
import jpa.learn.beans.extra.DynamicUserInfo2;
import jpa.learn.beans.extra.UserDto;
import jpa.learn.beans.extra.UserInfo1;
import jpa.learn.beans.extra.UserInfo2;
import jpa.learn.repos.user.ProjectionRepository;

@Service
public class UserProjectionService {

	@Autowired
	private ProjectionRepository repo;
	
	
	/*	Projection	by Interface- Closed   */
	public List<UserInfo1> fetchUserInfoByClosed(){
		
		return repo.findUserInfo1By();
	}
	
	/*	Projection	by Interface- Open	*/
	public List<UserInfo2> fetchUserInfoByOpen(){
		
		return repo.findUserInfo2By();
	}

	/*	Projection	by Class	*/
	public List<UserDto> fetchUserInfoByClassBased() {

		return repo.findUserDtoBy();
	}
	
	/*	Dynamic Content Projection- Multiple DTO to Single Repository method	*/
	public List<DynamicUserInfo1> findAllByUserInfo1(Class<DynamicUserInfo1> class1) {

		return repo.findAllBy(class1);
	}
	
	/*	Dynamic Content Projection- Multiple DTO to Single Repository method	*/
	public List<DynamicUserInfo2> findAllByUserInfo2(Class<DynamicUserInfo2> class2) {

		return repo.findAllBy(class2);
	}
	
	/*	Dynamic Content Projection- Multiple DTO to Single Repository method	*/
	public List<DynamicUserInfo1> getUserProjection(String firstName, String city) {
		
	    return repo.findByFirstNameAndAddress_City(firstName, city, DynamicUserInfo1.class);
	}
}
