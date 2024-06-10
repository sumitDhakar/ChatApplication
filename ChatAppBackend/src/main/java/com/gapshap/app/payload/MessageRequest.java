package com.gapshap.app.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {

	private String message;

	private String sender;

	private String recipient;

	private String conversationId;

	private String operationType;
//	private  = new ArrayList<>(); 

}
