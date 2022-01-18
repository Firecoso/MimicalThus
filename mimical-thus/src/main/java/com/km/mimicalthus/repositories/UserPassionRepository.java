package com.km.mimicalthus.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.km.mimicalthus.entities.User;
import com.km.mimicalthus.entities.UserPassion;

@Repository
public interface UserPassionRepository extends JpaRepository<UserPassion, Integer>{

	List<UserPassion> findByUserIn(List<User> selectedUsers);

	List<UserPassion> findByUser(User user);

}
