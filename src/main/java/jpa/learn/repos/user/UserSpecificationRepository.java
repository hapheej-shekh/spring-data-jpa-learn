package jpa.learn.repos.user;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import jpa.learn.beans.User;

public interface UserSpecificationRepository extends 
		CrudRepository<User, Integer>, JpaSpecificationExecutor<User> {

	// Exists in JpaSpecificationExecutor, So not needed but for understanding
	List<User> findAll(Specification<User> spec);
}
