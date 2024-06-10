package com.gapshap.app.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gapshap.app.model.chat.ChatFiles;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversationMessageHistoryResponse {

	private Long id;
	
	private Long sender;
	private Long recipient;
	
	   private String message;
	
	private String conversationId;

	private String sentAt;

//	private String recieved;
	@JsonIgnoreProperties(value= {"message"})
	private List<ChatFiles> chatFiles;
	
	@JsonIgnoreProperties(value="message")
	private List<ChatFiles> images;
	
	private String isSeen;

}
