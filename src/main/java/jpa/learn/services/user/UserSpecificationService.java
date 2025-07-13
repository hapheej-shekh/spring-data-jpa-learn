package jpa.learn.services.user;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jpa.learn.beans.User;
import jpa.learn.beans.extra.PageDetail;
import jpa.learn.repos.user.UserCriteriaRepository;
import jpa.learn.repos.user.UserSpecificationRepository;
import jpa.learn.util.UserSpecification;

@Service
public class UserSpecificationService {

	@Autowired
    private UserSpecificationRepository specificationRepo;
	@Autowired
	private UserCriteriaRepository criteriaRepo; // Criteria based Search
	

	/*	Specification based Pageable-> Sort & limit by pages */
    public Page<User> searchUsers(String city, PageDetail page) {

        Specification<User> spec = Specification.where(UserSpecification.hasCity(city));

        Sort sort = page.getSortDir().equalsIgnoreCase("asc") ? 
        		Sort.by(page.getSortBy()).ascending() : Sort.by(page.getSortBy()).descending();

        Pageable pageable = PageRequest.of(page.getPage(), page.getSize(), sort);

        return specificationRepo.findAll(spec, pageable);
    }
    
    public List<User> findByCity(String city) {
        
    	return specificationRepo.findAll(UserSpecification.hasCity(city));
    }

    public List<User> findByJoinDateBetween(Date startDate, Date endDate) {
        
    	return specificationRepo.findAll(UserSpecification.joinDateBetween(startDate, endDate));
    }

    public List<User> findByOldUserStatus(Boolean isOldUser) {
        
    	return specificationRepo.findAll(UserSpecification.isOldUser(isOldUser));
    }

    public List<User> findByfirstName(String firstName) {
        
    	return specificationRepo.findAll(UserSpecification.hasName(firstName));
    }

    /* Compound Method for All In One */
    public List<User> searchUsers(String firstName, String city, 
    		Boolean isOldUser, Date startDate, Date endDate) {
    	
        Specification<User> spec = Specification.where(UserSpecification.hasName(firstName))
                .and(UserSpecification.hasCity(city))
                .and(UserSpecification.isOldUser(isOldUser))
                .and(UserSpecification.joinDateBetween(startDate, endDate));

        return specificationRepo.findAll(spec);
    }
    
    
    /* Criteria based Search */
    public List<User> searchUsersCriteria(String name, String city, 
    		Boolean isOldUser, Date startDate, Date endDate) {
        
    	return criteriaRepo.searchUsers(name, city, isOldUser, startDate, endDate);
    }
}
