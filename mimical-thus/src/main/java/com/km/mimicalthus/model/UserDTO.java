package com.km.mimicalthus.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO implements Comparable<UserDTO>{
	
	private Integer userId;
	
	private Double similarity;
	
	private String email;
	
	private String name;
	
	private String surname;
	
	private Map<String,Integer> dataPoints;
	
	@Override
    public int compareTo(UserDTO otherUser) {
		return Double.compare(otherUser.getSimilarity(), this.similarity);
	}

}
