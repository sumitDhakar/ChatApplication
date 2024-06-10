package com.gapshap.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gapshap.app.model.User;
import com.gapshap.app.model.chat.UserActiveStatus;

public interface UserActiveStatusRepository extends JpaRepository<UserActiveStatus, Long>{

	
}
