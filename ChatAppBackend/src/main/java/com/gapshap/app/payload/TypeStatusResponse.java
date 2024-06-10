package com.gapshap.app.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypeStatusResponse {

	private Long id;
	private String userName;
	private String profileName;
	private String email;
	private String toEmail;
	private String conversationId;
	private String userStatus;
}
