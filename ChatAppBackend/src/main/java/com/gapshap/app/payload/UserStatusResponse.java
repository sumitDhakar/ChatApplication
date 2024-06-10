package com.gapshap.app.payload;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStatusResponse {

private Long id;
	
	private String lastSeen;
	
	private Boolean isOnline;
	
	private String status;
}
