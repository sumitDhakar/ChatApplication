package com.gapshap.app.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.gapshap.app.constants.AppConstants;
import com.gapshap.app.constants.UserStatus;
import com.gapshap.app.model.User;
import com.gapshap.app.model.chat.UserActiveStatus;
import com.gapshap.app.payload.UserStatusRequest;
import com.gapshap.app.repository.UserActiveStatusRepository;
import com.gapshap.app.service.IUserActiveStatusService;
import com.gapshap.app.service.IUserService;
import com.gapshap.app.socketconfiguration.SocketModule;

@Service
public class UserActiveStatusServiceImpl implements IUserActiveStatusService {

//	@Autowired
//	private UserRepository userRepository;

	@Autowired
	private IUserService userService;
	@Autowired
	private UserActiveStatusRepository activeStatusRepository;

	@Override
	public UserActiveStatus updateUserStatus(UserStatusRequest request) {

//		System.out.println(request.getEmail());
//	User user = this.userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UserNotFoundException(AppConstants.USER_NOT_FOUND));
		User user = this.userService.getUserByEmail(request.getEmail());
		UserActiveStatus userStatus = user.getUserStatus();
		if (request.getStatus().equals(UserStatus.ONLINE.toString())) {
			userStatus.setIsOnline(true);
			userStatus.setStatus(UserStatus.ONLINE);
		}

		else if (request.getStatus().equals(UserStatus.TYPING.toString())) {
			userStatus.setIsOnline(true);
			userStatus.setStatus(UserStatus.TYPING);
		} else if (request.getStatus().equals(UserStatus.OFFLINE.toString())) {
			userStatus.setIsOnline(false);
			userStatus.setStatus(UserStatus.OFFLINE);
		}

		userStatus.setLastSeen(LocalDateTime.now());
		UserActiveStatus save = this.activeStatusRepository.save(userStatus);

		return save;
	}

	@Override
	public ResponseEntity<?> getUserStatus(Long id) {
		Map<String, Object> response = new HashMap<>();
//		User user = this.userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(AppConstants.USER_NOT_FOUND));
		User user = this.userService.getUserById(id);
		response.put(AppConstants.MESSAGE, AppConstants.STATUS_RETERIVED);
		response.put(AppConstants.DATA_MESSAGE, user.getUserStatus());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
