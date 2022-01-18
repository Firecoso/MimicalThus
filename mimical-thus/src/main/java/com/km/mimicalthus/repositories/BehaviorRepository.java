package com.km.mimicalthus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.km.mimicalthus.entities.Behavior;

@Repository
public interface BehaviorRepository extends JpaRepository<Behavior, Integer>{

	Behavior findByName(String name);

}
