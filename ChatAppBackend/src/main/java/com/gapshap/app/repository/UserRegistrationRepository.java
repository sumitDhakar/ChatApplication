package com.gapshap.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gapshap.app.model.UserRegistration;

@Repository
public interface UserRegistrationRepository extends JpaRepository<UserRegistration, Long>{

   Optional<UserRegistration> findByEmail(String email);
	
   Optional<UserRegistration> findByEmailAndOtp(String email,String otp);
   
   Optional<UserRegistration> findByEmailAndIsActive(String email,Boolean isActive);
	
}
