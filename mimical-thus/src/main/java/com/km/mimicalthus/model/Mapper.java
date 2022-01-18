package com.km.mimicalthus.model;

import java.util.HashMap;

import com.km.mimicalthus.entities.Behavior;
import com.km.mimicalthus.entities.Passion;
import com.km.mimicalthus.entities.User;
import com.km.mimicalthus.entities.UserBehavior;
import com.km.mimicalthus.entities.UserPassion;

public class Mapper {

	public static UserDTO entityToDTO(User user) {
		UserDTO result = new UserDTO();
		
		result.setUserId(user.getUserId());
		result.setEmail(user.getEmail());
		result.setName(user.getName());
		result.setSurname(user.getSurname());
		
		result.setDataPoints(new HashMap<String, Integer>());
		
		return result;
	}

	public static User formToEntity(UserRegistrationForm form) {
		User user = new User();
		
		user.setEmail(form.getEmail());
		user.setName(form.getName());
		user.setSurname(form.getSurname());
		
		return user;
	}

	public static UserPassion createUserPassion(User user, UserPassionDTO pDTO, Passion pEntity) {
		UserPassion userPassion = new UserPassion();
		userPassion.setUser(user);
		userPassion.setPassion(pEntity);
		userPassion.setValue(pDTO.getValue());
		return userPassion;
	}

	public static UserBehavior createUserBehavior(User user, UserBehaviorDTO bDTO, Behavior bEntity) {
		UserBehavior userBehavior = new UserBehavior();
		userBehavior.setUser(user);
		userBehavior.setBehavior(bEntity);
		userBehavior.setValue(bDTO.getValue());
		return userBehavior;
	}

}
