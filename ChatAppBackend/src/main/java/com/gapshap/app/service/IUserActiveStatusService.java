package com.gapshap.app.service;

import org.springframework.http.ResponseEntity;

import com.gapshap.app.model.chat.UserActiveStatus;
import com.gapshap.app.payload.UserStatusRequest;

public interface IUserActiveStatusService {

	

	ResponseEntity<?> getUserStatus(Long id);

	UserActiveStatus updateUserStatus(UserStatusRequest request);
}
