package com.gapshap.app.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.gapshap.app.payload.ConversationRequest;
import com.gapshap.app.payload.MessageRequest;

public interface IMessageService {

	ResponseEntity<?> saveMessage(MessageRequest messageRequest,List<MultipartFile> files, String sendOrSaveMessageFor);

	ResponseEntity<?> updateMessage();

	ResponseEntity<?> getAllMessagesBySenderAndRecipient(ConversationRequest request);

	ResponseEntity<?> getMessageByConversationById(String conversationId,String currentUserEmail,String forWhichChats);
	
	ResponseEntity<?> deleteMessage(Long messageId,String userMessageDeleteEmail);
	ResponseEntity<?> updateMessageStatusToMarkedAsSeen(String  conversationId,String userMessageEmailToMarkAsSeen);


}
