package com.gapshap.app.service;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.gapshap.app.model.User;
import com.gapshap.app.payload.UserRequest;
import com.gapshap.app.payload.UserResponse;

public interface IUserService {

	public ResponseEntity<?> getAllUsers(String emailCurrent);

	public User getUserById(Long userId);

	public User getUserByEmail(String userEmail);

	
	
	public ResponseEntity<?> getUserByEmailOrUserName(String value);

	public ResponseEntity<?> updateUser(UserRequest request,MultipartFile file, Principal p);

	UserResponse userToUserResponse(User user);

	UserResponse userToSearchUserResponse(User user);
}
