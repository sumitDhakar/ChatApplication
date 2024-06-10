package com.gapshap.app.service;

import org.springframework.http.ResponseEntity;

import com.gapshap.app.payload.NotificationRequest;

public interface INotificationService {

	ResponseEntity<?> sendNotification(NotificationRequest request);
	
	ResponseEntity<?> updateNotification(NotificationRequest request);
	
}
