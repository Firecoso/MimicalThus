package com.km.mimicalthus.model;

import java.util.List;

import lombok.Data;

@Data
public class UserRegistrationForm {
	
	private String email;
	
	private String name;
	
	private String surname;
	
	private List<UserPassionDTO> passions;
	
	private List<UserBehaviorDTO> behaviors;

}
