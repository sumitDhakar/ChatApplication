package com.gapshap.app.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class UserRegistration {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String userName;
	
	private String email;
	
	private String password;
	
	private String otp;
	
	private Boolean isActive = false;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime updatedAt;
}
