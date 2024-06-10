package com.gapshap.app.service.impl;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.gapshap.app.constants.AppConstants;
import com.gapshap.app.constants.InvitationStatus;
import com.gapshap.app.exception.ResourceAlreadyExists;
import com.gapshap.app.exception.ResourceNotFoundException;
import com.gapshap.app.model.User;
import com.gapshap.app.model.chat.InvitationRequest;
import com.gapshap.app.payload.ContactRequest;
import com.gapshap.app.payload.InvitationResponse;
import com.gapshap.app.payload.InvitationSendRequest;
import com.gapshap.app.payload.UpdateInvitationRequest;
import com.gapshap.app.repository.InvitationRepository;
import com.gapshap.app.service.IContactService;
import com.gapshap.app.service.IInvitationService;
import com.gapshap.app.service.IUserService;
import com.gapshap.app.socketconfiguration.SocketModule;

@Service
public class InvitationServiceImpl implements IInvitationService {

	@Autowired
	private InvitationRepository invitationRepository;

	@Autowired
	private IContactService contactService;

	@Autowired
	private SocketModule socketModule;

	@Autowired
	private IUserService userService;

	@Autowired
	private ModelMapper mapper;

	@Override
	public ResponseEntity<?> sendInvitation(InvitationSendRequest request) {
		Map<String, Object> response = new HashMap<>();

		Boolean existsBySenderAndRecipient = this.invitationRepository.existsBySenderAndRecipientOrRecipientAndSender(
				request.getSender(), request.getRecipient(), request.getRecipient(), request.getRecipient());
		if (existsBySenderAndRecipient) {
			throw new ResourceAlreadyExists(AppConstants.INVITATION_ALREADY_SENT);
		}
		InvitationRequest invitation = new InvitationRequest();

		invitation.setCreatedAt(LocalDateTime.now());
		invitation.setRequestStatus(InvitationStatus.NEW);

		User sender = this.userService.getUserByEmail(request.getSender());
		User recipient = this.userService.getUserByEmail(request.getRecipient());
		invitation.setSender(sender);
		invitation.setRecipient(recipient);
		InvitationRequest savedInvitation = this.invitationRepository.save(invitation);

		response.put(AppConstants.MESSAGE, AppConstants.INVITATION_SENT);

		if (savedInvitation != null) {
			this.socketModule.sendMessage(savedInvitation.getRecipient().getEmail(), AppConstants.SEND_INVITATION,
					savedInvitation);
		}
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<?> updateInvitaion(UpdateInvitationRequest request) {
		Map<String, Object> response = new HashMap<>();
		InvitationRequest invitation = this.invitationRepository.findById(request.getId())
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.INVITATION_NOT_FOUND));

		if (request.getRequestStatus().equals(InvitationStatus.ACCEPTED)) {
			InvitationRequest invitation2 = new InvitationRequest();

			invitation2.setCreatedAt(LocalDateTime.now());
			invitation2.setRequestStatus(request.getRequestStatus());

			invitation2.setSender(invitation.getRecipient());
			invitation2.setRecipient(invitation.getRecipient());

			invitation.setRequestStatus(request.getRequestStatus());
			invitation.setInvitationMessage("You Accepted His/Her friend request");
			invitation2.setRequestStatus(request.getRequestStatus());
			invitation2.setInvitationMessage("Your friend request has been Accepted");

			this.contactService.createContact(
					new ContactRequest(invitation.getSender().getEmail(), invitation.getRecipient().getEmail()));

			this.invitationRepository.save(invitation);
			this.invitationRepository.save(invitation2);
			response.put(AppConstants.MESSAGE, AppConstants.INVITATION_UPDATE_SUCCESS);
			return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
		} else if (request.getRequestStatus().equals(InvitationStatus.REJECTED)) {
			return this.deleteInvitaion(invitation.getId());

		}

		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

	public ResponseEntity<?> getAllNotificationsOfUser(Principal p, Integer pageNo, Integer pageSize) {

//		User user = this.userRepository.findByEmail(p.getName())
//				.orElseThrow(() -> new UserNotFoundException(AppConstants.USER_NOT_FOUND));
		User user = this.userService.getUserByEmail(p.getName());
		Page<InvitationRequest> invitations = this.invitationRepository
				.findByRecipientAndRequestStatusOrderByCreatedAtDesc(user, InvitationStatus.NEW,
						PageRequest.of(pageNo, pageSize));

		Map<String, Object> response = new HashMap<>();

		response.put(AppConstants.MESSAGE, AppConstants.INVITATION_RETREIVED);
		response.put(AppConstants.DATA_MESSAGE, invitations.map(i -> this.mapper.map(i, InvitationResponse.class)));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getInvitationBySenderAndRecipient(InvitationSendRequest request) {

		Map<String, Object> response = new HashMap<>();
		User sender = this.userService.getUserByEmail(request.getSender());
		User recipient = this.userService.getUserByEmail(request.getRecipient());
		Optional<InvitationRequest> present = this.invitationRepository.findBySenderAndRecipient(sender, recipient);
		if (present.isEmpty()) {

			Optional<InvitationRequest> isHeSent = this.invitationRepository.findBySenderAndRecipient(recipient,
					sender);
			if (isHeSent.isEmpty()) {
				response.put(AppConstants.MESSAGE, AppConstants.INVITATION_NOT_FOUND);

				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
			response.put("sentBy", AppConstants.RECIPIENT);
			response.put(AppConstants.MESSAGE, AppConstants.INVITATION_RETREIVED);
			response.put(AppConstants.DATA_MESSAGE, present.get());
			return new ResponseEntity<>(response, HttpStatus.OK);

		}
		response.put("sentBy", AppConstants.SENDER);
		response.put(AppConstants.MESSAGE, AppConstants.INVITATION_RETREIVED);
		response.put(AppConstants.DATA_MESSAGE, present.get());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> deleteInvitaion(Long id) {
		Map<String, Object> response = new HashMap<>();
		this.invitationRepository.deleteById(id);
		response.put(AppConstants.MESSAGE, AppConstants.INVITAION_DELETED);
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

	@Override
	public ResponseEntity<?> getAllInvitationOfUserByEmail(String email) {
		User user = this.userService.getUserByEmail(email);
		Sort sortBy = Sort.by(Sort.Direction.DESC, "createdAt");
		List<InvitationRequest> findAllByRecipient = this.invitationRepository.findAllByRecipient(user, sortBy);
		return ResponseEntity.ok(findAllByRecipient);
	}

}
