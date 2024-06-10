package com.gapshap.app.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gapshap.app.payload.MessageRequest;
import com.gapshap.app.service.IMessageService;

@RestController
@RequestMapping("/gapshap/chat/")
@CrossOrigin("*")
public class ChatController {

	@Autowired
	private IMessageService messageService;

//	@Autowired
//	private SimpMessagingTemplate messagingTemplate;

	@PostMapping(path = "sendMessage", consumes = { "multipart/form-data", "application/octet-stream" })
	private ResponseEntity<?> sendMessage(@RequestPart(value = "filesData", required = false) List<MultipartFile> file,
			@RequestPart(name = "message") MessageRequest messageRequest,
			@RequestParam("sendOrSaveMessageFor") String sendOrSaveMessageFor) {

		return this.messageService.saveMessage(messageRequest, file, sendOrSaveMessageFor);
	}

	

	@GetMapping("oldMessageHistory")
	public ResponseEntity<?> getAllMessageByConversationIdId(@RequestParam(name="conversationId") String conversationId,
			@RequestParam("forWhichChats") String forWhichChats, Principal p) {
		return this.messageService.getMessageByConversationById(conversationId, p.getName(), forWhichChats);
	}
	
	
	@GetMapping("markedAsRead")
	public ResponseEntity<?> updateIsSeenForCurrentUser(@RequestParam(name="conversationId") String conversationId,
			 Principal p) {
		return this.messageService.updateMessageStatusToMarkedAsSeen(conversationId, p.getName());
	}
	

	@DeleteMapping("deleteMessage")
	public ResponseEntity<?> deleteMessages(@RequestParam(name="deleteMessage") Long messageId, Principal p) {
		return this.messageService.deleteMessage(messageId, p.getName());
	}

}
