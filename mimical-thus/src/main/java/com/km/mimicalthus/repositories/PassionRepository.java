package com.km.mimicalthus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.km.mimicalthus.entities.Passion;

@Repository
public interface PassionRepository extends JpaRepository<Passion, Integer>{

	Passion findByName(String name);

}
