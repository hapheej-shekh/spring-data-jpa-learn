package jpa.learn.util;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import org.springframework.data.jpa.domain.Specification;

import jpa.learn.beans.User;

/*	User Specification, to build Criteria based Dynamic queries */
public class UserSpecification {

    public static Specification<User> hasName(String firstName) {
    	
        return (root, query, cb) -> 
            (firstName == null || firstName.isBlank()) ? null :
            cb.equal(root.get("name"), firstName);
    }

    public static Specification<User> hasCity(String city) {
    	
        return (root, query, cb) -> 
            (city == null || city.isBlank()) ? null :
            cb.equal(root.join("address").get("city"), city);
    }

    public static Specification<User> isOldUser(Boolean isOldUser) {
    	
        return (root, query, cb) -> {
        	
            if (isOldUser == null)
                return null;
            
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.YEAR, -1); // 1 year before today
            Date oneYearAgo = cal.getTime();

            if (isOldUser) {
                return cb.lessThanOrEqualTo(root.join("address").get("joinDate"), oneYearAgo);
            } else {
                return cb.greaterThan(root.join("address").get("joinDate"), oneYearAgo);
            }
        };
    }

    public static Specification<User> joinDateBetween(Date startDate, Date endDate) {
    	
        return (root, query, cb) -> {
        	
            if (startDate == null && endDate == null)
                return null;

            if (startDate != null && endDate != null) {
                return cb.between(root.join("address").get("joinDate"), startDate, endDate);
            }
            if (startDate != null) {
                return cb.greaterThanOrEqualTo(root.join("address").get("joinDate"), startDate);
            }
            return cb.lessThanOrEqualTo(root.join("address").get("joinDate"), endDate);
        };
    }
    
    public static Specification<User> isLongTermUser() {
    	
        return (root, query, builder) -> {
          LocalDate date = LocalDate.now().minusYears(2);
          return builder.lessThan(root.join("address").get("joinDate"), date);
        };
	}
}
