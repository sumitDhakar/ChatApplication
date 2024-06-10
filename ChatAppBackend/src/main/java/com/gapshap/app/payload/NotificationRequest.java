package com.gapshap.app.payload;

import java.time.LocalDateTime;

import com.gapshap.app.model.User;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {
	
	private Long id;
	
	private String sender;

	private String recipient;

	private String message;

	private LocalDateTime time;

	private Boolean isSeen;

	private Boolean isClear = false;
}
