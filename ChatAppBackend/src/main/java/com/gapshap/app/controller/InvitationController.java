package com.gapshap.app.controller;


import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.gapshap.app.exception.ResourceNotFoundException;
import com.gapshap.app.payload.InvitationSendRequest;
import com.gapshap.app.payload.UpdateInvitationRequest;
import com.gapshap.app.service.IInvitationService;

@RestController
@RequestMapping("/gapshap/invitation")
@CrossOrigin("*")
public class InvitationController {

//	@Autowired
//	private SimpMessagingTemplate messageTemplate;

	@Autowired
	private IInvitationService invitationService;

	@PostMapping("/invite")
	public ResponseEntity<?> sendInvite(@RequestBody InvitationSendRequest request, Principal p) {
		if(p.getName().equals(request.getRecipient())) {
			throw new ResourceNotFoundException("You Are requesting To yourSelf");
		}
//		this.messageTemplate.convertAndSend("/user/"+request.getRecipient(),request);
		request.setSender(p.getName());
		return this.invitationService.sendInvitation(request);

	}

	@PutMapping("/update")
	public ResponseEntity<?> updateInvitation(@RequestPart(name="invitationRequest") UpdateInvitationRequest invitationRequest) {
System.err.println(invitationRequest.getId()+"   "+invitationRequest.getRequestStatus());
//		this.messageTemplate.convertAndSend("/user/"+request.getRecipient(),request);
		return this.invitationService.updateInvitaion(invitationRequest);

	}
	
	
	@DeleteMapping("/invite/delete")
	public ResponseEntity<?> deleteInvite(Long id, Principal p) {
		ResponseEntity<?> response = this.invitationService.deleteInvitaion(id);
//		this.messageTemplate.convertAndSend("/user/"+p.getName(),response);
		return response;
	}

	@GetMapping("/getAll")
	public ResponseEntity<?> getAllInvitationsOfCurrentUSer(Principal p) {
		ResponseEntity<?> response = this.invitationService.getAllInvitationOfUserByEmail(p.getName());
//		this.messageTemplate.convertAndSend("/user/"+p.getName(),response);
		System.err.println(response);
		return response;
	}

}
