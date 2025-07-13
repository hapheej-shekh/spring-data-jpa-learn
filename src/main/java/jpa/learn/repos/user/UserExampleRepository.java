package jpa.learn.repos.user;

import org.springframework.data.jpa.repository.JpaRepository;

import jpa.learn.beans.User;

public interface UserExampleRepository extends JpaRepository<User, Integer> {

}
