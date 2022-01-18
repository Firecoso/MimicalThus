package com.km.mimicalthus.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.km.mimicalthus.entities.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegistrationResponseWrapper {
	
	private User user;
	
	private String errorMessage;

}
