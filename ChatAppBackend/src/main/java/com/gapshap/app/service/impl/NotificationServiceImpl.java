package com.gapshap.app.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.gapshap.app.constants.AppConstants;
import com.gapshap.app.exception.ResourceNotFoundException;
import com.gapshap.app.exception.UserNotFoundException;
import com.gapshap.app.model.User;
import com.gapshap.app.model.chat.Notifications;
import com.gapshap.app.payload.NotificationRequest;
import com.gapshap.app.payload.NotificationResponse;
import com.gapshap.app.payload.SearchUserResponse;
import com.gapshap.app.repository.NotificationRepository;
import com.gapshap.app.repository.UserRepository;
import com.gapshap.app.service.INotificationService;
import com.gapshap.app.service.IUserService;

@Service
public class NotificationServiceImpl implements INotificationService {

	@Autowired
	private NotificationRepository notificationRepository;

//	@Autowired
//	private UserRepository userRepository;
	@Autowired
	private IUserService userService;

	@Autowired
	private ModelMapper mapper;

	@Override
	public ResponseEntity<?> sendNotification(NotificationRequest request) {
		Map<String, Object> response = new HashMap<>();
		Notifications notification = new Notifications();
//		 User sender= this.userRepository.findByEmail(request.getSender()).orElseThrow(() -> new UserNotFoundException(AppConstants.USER_NOT_FOUND));
//		 User recipient= this.userRepository.findByEmail(request.getSender()).orElseThrow(() -> new UserNotFoundException(AppConstants.USER_NOT_FOUND));
		User sender = this.userService.getUserByEmail(request.getSender());
		User recipient = this.userService.getUserByEmail(request.getRecipient());

		notification.setSender(sender);
		notification.setRecipient(recipient);
		notification.setTime(LocalDateTime.now());
		this.notificationRepository.save(notification);
		response.put(AppConstants.MESSAGE, AppConstants.NOTIFICATION_SENT);

		SearchUserResponse from = new SearchUserResponse();
		from.setProfileName(sender.getProfileName());
		from.setUserName(sender.getUserName());
		from.setBio(sender.getBio());

		response.put(AppConstants.DATA_MESSAGE, new NotificationResponse(null, from));
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

	@Override
	public ResponseEntity<?> updateNotification(NotificationRequest request) {
		Notifications notification = this.notificationRepository.findById(request.getId())
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.NOTIFICATION_NOT_FOUND));
		notification.setIsClear(request.getIsClear());
		notification.setIsSeen(request.getIsSeen());
		this.notificationRepository.save(notification);
		Map<String, Object> response = new HashMap<>();
		response.put(AppConstants.MESSAGE, AppConstants.NOTIFICATION_UPDATED);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
