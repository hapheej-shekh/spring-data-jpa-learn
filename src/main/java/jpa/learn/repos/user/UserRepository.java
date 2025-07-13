package jpa.learn.repos.user;

import org.springframework.data.repository.CrudRepository;

import jpa.learn.beans.User;

public interface UserRepository extends CrudRepository<User, Integer> {

}
