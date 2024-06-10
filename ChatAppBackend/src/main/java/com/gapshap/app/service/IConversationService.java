package com.gapshap.app.service;

import java.security.Principal;

import org.springframework.http.ResponseEntity;

import com.gapshap.app.model.chat.Conversation;
import com.gapshap.app.model.chat.Message;
import com.gapshap.app.payload.ConversationRequest;
import com.gapshap.app.payload.ConversationResponse;

public interface IConversationService {

	ConversationResponse conversationToConversationResponse(Conversation c);
	
	ResponseEntity<?> createConversation(ConversationRequest request, Principal p);

	ResponseEntity<?> getConversation(String receiverEmail, Principal p);

	ResponseEntity<?> getAllConversationsOfCurrentUser(Principal p);

	ResponseEntity<?> deleteConversation(ConversationRequest request, Principal p);

	void updateConversationHistoryMessageOfUser(Message m);

}
