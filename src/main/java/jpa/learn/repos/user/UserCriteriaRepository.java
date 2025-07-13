package jpa.learn.repos.user;

import java.util.Date;
import java.util.List;

import jpa.learn.beans.User;

// This interface is implemented by UserCriteriaRepositoryImpl
public interface UserCriteriaRepository {
	
    List<User> searchUsers(String name, String city, Boolean isOldUser, 
    		Date startDate, Date endDate);
}
