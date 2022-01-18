package com.km.mimicalthus.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="Behaviors")
@Data
public class Behavior {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="behavior_id")
	private Integer behaviorId;
	
	@Column
	private String name;

}
