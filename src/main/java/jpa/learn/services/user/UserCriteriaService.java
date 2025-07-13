package jpa.learn.services.user;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jpa.learn.beans.User;
import jpa.learn.repos.user.UserCriteriaRepository;

@Service
public class UserCriteriaService {

	@Autowired
	private UserCriteriaRepository repo;
	

	public List<User> searchUsers(String firstName, String city, Boolean isOlduser, 
			Date startDate, Date endDate) {
	    
		return repo.searchUsers(firstName, city, isOlduser, startDate, endDate);
	}
}
