package com.gapshap.app.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gapshap.app.exception.ResourceNotFoundException;
import com.gapshap.app.payload.ConversationRequest;
import com.gapshap.app.service.IConversationService;

@RestController
@RequestMapping("/gapshap/conversation/")
@CrossOrigin("*")
public class ConversationController {

	@Autowired
	private IConversationService conversationService;
	
	
	@PostMapping
	public ResponseEntity<?> createConversation(@RequestBody ConversationRequest request,Principal p){
		if(p.getName().equals(request.getRecipient())) {
			throw new ResourceNotFoundException("You Are requesting To yourSelf");
		}
		return this.conversationService.createConversation(request,p);
	}
	
	@GetMapping("get")
	public ResponseEntity<?> getConversationOfRecipient(@RequestParam("receiverEmail") String receiverEmail,Principal p){
		if(p.getName().equals(receiverEmail)) {
			throw new ResourceNotFoundException("You Are requesting To yourSelf");
		}
		
		return this.conversationService.getConversation(receiverEmail, p);
	}
	
	
	@GetMapping("getConversations")
	public ResponseEntity<?> getAllConversationsOfCurrentUser(Principal p){
		
		return this.conversationService.getAllConversationsOfCurrentUser( p);
	}
	
}
