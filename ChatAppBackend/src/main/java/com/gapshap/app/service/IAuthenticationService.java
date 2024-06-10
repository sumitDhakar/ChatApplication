package com.gapshap.app.service;

import java.security.Principal;

import org.springframework.http.ResponseEntity;

import com.gapshap.app.payload.LoginRequest;
import com.gapshap.app.payload.ResendOtpRequest;
import com.gapshap.app.payload.UserRegistrationRequest;
import com.gapshap.app.payload.VerificationRequest;

public interface IAuthenticationService {

	ResponseEntity<?> registration(UserRegistrationRequest request);
	
	ResponseEntity<?> verifyUser(VerificationRequest request);
	
	ResponseEntity<?> loginUser(LoginRequest loginRequest);
	
	ResponseEntity<?> getCurrentUser(Principal p);
	
	ResponseEntity<?> resendOtp(ResendOtpRequest resendRequest);
}
