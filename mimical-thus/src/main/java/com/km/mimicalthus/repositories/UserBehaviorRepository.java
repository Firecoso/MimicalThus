package com.km.mimicalthus.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.km.mimicalthus.entities.User;
import com.km.mimicalthus.entities.UserBehavior;

@Repository
public interface UserBehaviorRepository extends JpaRepository<UserBehavior, Integer> {
	
	List<UserBehavior> findByUserIn(List<User> users);

}
