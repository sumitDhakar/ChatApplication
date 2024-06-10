package com.gapshap.app.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import com.gapshap.app.payload.InvitationSendRequest;
import com.gapshap.app.payload.UpdateInvitationRequest;

@Repository
public interface IInvitationService {

	ResponseEntity<?> sendInvitation(@RequestBody InvitationSendRequest request);
	ResponseEntity<?> getAllInvitationOfUserByEmail(String email);
	
	ResponseEntity<?> updateInvitaion(@RequestBody UpdateInvitationRequest request);
	
	ResponseEntity<?> getInvitationBySenderAndRecipient(@RequestBody InvitationSendRequest request);

	ResponseEntity<?> deleteInvitaion(Long id);
	
}
