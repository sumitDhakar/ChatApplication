package com.gapshap.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gapshap.app.model.Roles;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Integer>{

}
