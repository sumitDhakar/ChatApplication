package com.gapshap.app.service.impl;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.gapshap.app.constants.AppConstants;
import com.gapshap.app.exception.ResourceNotFoundException;
import com.gapshap.app.model.User;
import com.gapshap.app.model.chat.Conversation;
import com.gapshap.app.model.chat.Message;
import com.gapshap.app.payload.CarouselItem;
import com.gapshap.app.payload.ConversationRequest;
import com.gapshap.app.payload.ConversationResponse;
import com.gapshap.app.repository.ConversationRepository;
import com.gapshap.app.repository.MessageRepository;
import com.gapshap.app.service.IConversationService;
import com.gapshap.app.service.IUserService;
import com.gapshap.app.socketconfiguration.SocketModule;

@Service
public class ConversationServiceImpl implements IConversationService {

	@Autowired
	private ConversationRepository conversationRepository;

	@Autowired
	private MessageRepository messageRepository;
	@Autowired
	private IUserService userService;

	@Autowired
	private SocketModule socketModule;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public ConversationResponse conversationToConversationResponse(Conversation c) {
            
		Long unSeenCount = this.messageRepository.countUnseenMessagesByConversationIdAndCurrentUser(c.getConversationId(), c.getSender().getEmail()); // You can perform any operation here

		// Map Conversation to ConversationResponse
		ConversationResponse conversationResponse = modelMapper.map(c, ConversationResponse.class);

		// Set the unSeenMessageCount field
		conversationResponse.setUnSeenMessageCount(unSeenCount);

		return conversationResponse;
	}

	@Override
	public ResponseEntity<?> createConversation(ConversationRequest request, Principal p) {
		Map<String, Object> response = new HashMap<>();

		User sender = this.userService.getUserByEmail(p.getName());
		User recipient = this.userService.getUserByEmail(request.getRecipient());
		Optional<Conversation> conversation = this.conversationRepository.findBySenderAndRecipient(sender.getId(),
				recipient.getId());
		if (conversation.isPresent()) {
			response.put(AppConstants.MESSAGE, AppConstants.CONVERSATION_RETREIVED);
			response.put(AppConstants.DATA_MESSAGE, conversation.get());
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		Conversation conver = new Conversation();
		String random = UUID.randomUUID().toString();
		conver.setConversationId(random);
		conver.setSender(sender);
		conver.setRecipient(recipient);
		conver.setCreatedAt(LocalDateTime.now());
		conver.setLastMessageAt(LocalDateTime.now());

		Conversation conver1 = new Conversation();
		conver1.setConversationId(random);
		conver1.setSender(recipient);
		conver1.setRecipient(sender);
		conver1.setCreatedAt(LocalDateTime.now());
		conver1.setLastMessageAt(LocalDateTime.now());
		Conversation savedConversation = this.conversationRepository.save(conver);
		Conversation sendConversationData = this.conversationRepository.save(conver1);
		if (savedConversation != null) {
			this.socketModule.sendMessage(savedConversation.getRecipient().getEmail(), AppConstants.GET_CONVERSATION,
					sendConversationData);
		}
		response.put(AppConstants.MESSAGE, AppConstants.CONVERSATION_RETREIVED);
		response.put(AppConstants.DATA_MESSAGE, savedConversation);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<?> getConversation(String receiverEmail, Principal p) {
		Map<String, Object> response = new HashMap<>();
//		User sender = this.userRepository.findByEmail(p.getName()).orElseThrow(() -> new UserNotFoundException(AppConstants.USER_NOT_FOUND));
		User sender = this.userService.getUserByEmail(p.getName());
		User reciever = this.userService.getUserByEmail(receiverEmail);
		Optional<Conversation> conversation = this.conversationRepository.findBySenderAndRecipient(sender.getId(),
				reciever.getId());

		if (conversation.isPresent()) {

			response.put(AppConstants.MESSAGE, AppConstants.CONVERSATION_RETREIVED);
			response.put(AppConstants.DATA_MESSAGE, conversation);
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		}
		response.put(AppConstants.MESSAGE, AppConstants.CONVERSATION_NOT_FOUND);
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

	}

	@Override
	public ResponseEntity<?> deleteConversation(ConversationRequest request, Principal p) {
		User sender = this.userService.getUserByEmail(p.getName());
		User recipient = this.userService.getUserByEmail(request.getRecipient());
		Conversation conversation = this.conversationRepository
				.findBySenderAndRecipient(sender.getId(), recipient.getId())
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.CONVERSATION_NOT_FOUND));
		this.conversationRepository.delete(conversation);
		Map<String, Object> response = new HashMap<>();
		response.put(AppConstants.MESSAGE, AppConstants.CONVERSATION_DELETED_SUCCESS);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getAllConversationsOfCurrentUser(Principal p) {
		Map<String, Object> response = new HashMap<>();
		User user = this.userService.getUserByEmail(p.getName());

		List<Conversation> conversationListOFUser = this.conversationRepository
				.getAllConversationsOfGivenUserId(user.getId());

		List<CarouselItem> carouselItems = conversationListOFUser.stream().map(conversation -> {
			Long id = conversation.getRecipient().getId();
			String name = conversation.getRecipient().getUserName();
			String avatar = conversation.getRecipient().getProfileName(); // Assuming profileName contains the avatar
																			// URL
			boolean isActive = conversation.getRecipient().getUserStatus().getIsOnline(); // Invert isDeleted to
																							// isActive
			return new CarouselItem(id, name, avatar, isActive);
		}).collect(Collectors.toList());

		List<ConversationResponse> listOfConversationResponse = conversationListOFUser.stream()
				.map(conversation -> this.conversationToConversationResponse(conversation))
				.collect(Collectors.toList());

		response.put(AppConstants.DATA_MESSAGE, listOfConversationResponse);
		response.put(AppConstants.DATA_CarouselItem, carouselItems);

		response.put(AppConstants.MESSAGE, AppConstants.CONVERSATION_RETREIVED);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public void updateConversationHistoryMessageOfUser(Message m) {

		List<Conversation> conversation = this.conversationRepository.findByConversationId(m.getConversationId());

		for (Conversation c : conversation) {
			c.setLastMessageAt(m.getSentAt());
			if (Objects.nonNull(m.getMessage())) {
				c.setLastMessage(m.getMessage());
			} else {
				c.setLastMessage("file shared");
			}
			this.conversationRepository.save(c);
		}

	}

}
