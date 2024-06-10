package com.gapshap.app.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gapshap.app.constants.AppConstants;
import com.gapshap.app.exception.ResourceNotFoundException;
import com.gapshap.app.exception.UserNotFoundException;
import com.gapshap.app.fileServicel.IFileService;
import com.gapshap.app.model.User;
import com.gapshap.app.model.chat.ChatFiles;
import com.gapshap.app.model.chat.ChatGroupMembers;
import com.gapshap.app.model.chat.GroupChat;
import com.gapshap.app.model.chat.Message;
import com.gapshap.app.payload.ConversationMessageHistoryResponse;
import com.gapshap.app.payload.ConversationRequest;
import com.gapshap.app.payload.MessageRequest;
import com.gapshap.app.repository.ChatFilesRepository;
import com.gapshap.app.repository.MessageRepository;
import com.gapshap.app.service.IConversationService;
import com.gapshap.app.service.IGroupService;
import com.gapshap.app.service.IMessageService;
import com.gapshap.app.service.IUserService;
import com.gapshap.app.socketconfiguration.SocketModule;

@Service
public class MessageServiceImpl implements IMessageService {

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private ChatFilesRepository chatFilesRepository;

	@Autowired
	private IFileService fileSerive;
//	@Autowired
//	private IContactService contactService;

	@Autowired
	private IUserService userService;

	@Autowired
	private IGroupService groupService;

	@Autowired
	private IConversationService conversationService;
	@Autowired
	private SocketModule socketModule;

	@Override
	public ResponseEntity<?> saveMessage(MessageRequest messageRequest, List<MultipartFile> files,
			String sendOrSaveMessageFor) {

//		Optional<Contacts> contact = this.contactService.findContactByOwnerAndContact(messageRequest.getSender(),
//				messageRequest.getRecipient());
//		if (contact.isEmpty()) {
//
//			this.contactService.createContact(new ContactRequest(sender.getEmail(), recipient.getEmail()));
//		}
		List<ChatGroupMembers> members = new ArrayList<>();
		Message message = new Message();
		if (sendOrSaveMessageFor.equals("group")) {
			GroupChat gr = this.groupService.getGroupChatByConversationId(messageRequest.getConversationId());

			members = gr.getChatGroupMembers();
			message.setRecipient(null);
		}

		User sender = this.userService.getUserByEmail(messageRequest.getSender());


		if (sendOrSaveMessageFor.equals("single")) {

			message.setRecipient(this.userService.getUserByEmail(messageRequest.getRecipient()));
		}

		Map<String, Object> response = new HashMap<>();

		message.setSentAt(LocalDateTime.now());
		message.setMessage(messageRequest.getMessage());
		message.setSender(sender);

		message.setIsSeen(false);
		message.setConversationId(messageRequest.getConversationId());
		Message savedMessage = this.messageRepository.save(message);

		if (Objects.nonNull(files)) {
			savedMessage.setChatFiles(this.saveChatFiles(savedMessage, files));
		}
		ConversationMessageHistoryResponse messageToServer = this.messageToMessageResponse(savedMessage);
		List<ChatFiles> images = new ArrayList<>();
		if (messageToServer.getChatFiles() != null) {
			ListIterator<ChatFiles> iterator = messageToServer.getChatFiles().listIterator();
			while (iterator.hasNext()) {
				ChatFiles f = iterator.next();
				if (f.getFileName().endsWith(".jpg") || f.getFileName().endsWith(".png")
						|| f.getFileName().endsWith(".jpeg")) {
					images.add(f);
					iterator.remove();
				}

			}
			messageToServer.setImages(images);
		}

		if (messageToServer != null) {
			if (sendOrSaveMessageFor.equals("group"))
				for (ChatGroupMembers chatGroupMembers : members) {
					if (!messageRequest.getSender().equals(chatGroupMembers.getUserId().getEmail()))
						this.socketModule.sendMessage(chatGroupMembers.getUserId().getEmail(),
								AppConstants.GET_MESSAGE_GROUP, messageToServer);
				}
			else {
				this.socketModule.sendMessage(savedMessage.getRecipient().getEmail(), AppConstants.GET_MESSAGE,
						messageToServer);
			}
			this.conversationService.updateConversationHistoryMessageOfUser(savedMessage);
		}

		response.put(AppConstants.MESSAGE, AppConstants.MESSAGE_DELIVERED);
		response.put(AppConstants.DATA_MESSAGE, messageToServer);

		return new ResponseEntity<>(response, HttpStatus.CREATED);

	}

	private List<ChatFiles> saveChatFiles(Message m, List<MultipartFile> files) {
		List<ChatFiles> chatfiles = new ArrayList<>();

		for (MultipartFile file : files) {
			ChatFiles cf = new ChatFiles();
			cf.setFileSize(file.getSize());

			cf.setFileName(file.getOriginalFilename());
			cf.setServerFileName(this.fileSerive.uploadFileInFolderForAnyFiles(file, "User"));
			cf.setMessage(m);
			chatfiles.add(cf);

		}
		List<ChatFiles> allSavedFiles = this.chatFilesRepository.saveAll(chatfiles);
		return allSavedFiles;
	}


	@Override
	public ResponseEntity<?> updateMessage() {

		return null;
	}

	@Override
	public ResponseEntity<?> getAllMessagesBySenderAndRecipient(ConversationRequest request) {
		User sender = this.userService.getUserByEmail(request.getSender());

		Map<String, Object> response = new HashMap<>();
		List<Message> message = this.messageRepository.findBySenderAndRecipient(sender.getEmail(),
				request.getRecipient());
		response.put(AppConstants.MESSAGE, AppConstants.MESSAGE_RETERIVED);
		response.put(AppConstants.DATA_MESSAGE, message);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getMessageByConversationById(String conversationId, String currentUserEmail,
			String forWhichChats) {
		Map<String, Object> response = new HashMap<>();
		List<Message> messages = new ArrayList<>();
		if (forWhichChats.equals("group"))
			messages = this.messageRepository.findByConversationIdOrderBySentAt(conversationId);
		else if (forWhichChats.equals("single"))
			messages = this.messageRepository.findByConversationIdOrderBySentAtForSingleUser(conversationId,
					currentUserEmail);
		else {
			throw new ResourceNotFoundException("Invalid request can not be processed");
		}
		List<ConversationMessageHistoryResponse> res = messages.stream().map(m -> this.messageToMessageResponse((m)))
				.collect(Collectors.toList());
		List<ChatFiles> allChatFiles =new ArrayList<>();
		res.stream().forEach(m -> {
			ListIterator<ChatFiles> iterator = m.getChatFiles().listIterator();
			List<ChatFiles> images = new ArrayList<>();
			while (iterator.hasNext()) {
				ChatFiles f = iterator.next();
				if(f.getIsDeleted()) {
					iterator.remove();
				}
				else {
				allChatFiles.add(f);
				if (f.getFileName().endsWith(".jpg") || f.getFileName().endsWith(".png")
						|| f.getFileName().endsWith(".jpeg")) {
					images.add(f);
					iterator.remove();
				}
				}
			}
			m.setImages(images);
		});
		
		Map<LocalDate, List<ConversationMessageHistoryResponse>> list = res.stream()
				.collect(Collectors.groupingBy(m -> LocalDateTime.parse(m.getSentAt()).toLocalDate()));
		response.put(AppConstants.MESSAGE, AppConstants.MESSAGE_RETRIEVED_SUCCESS);
		response.put(AppConstants.DATA_MESSAGE, list);
		response.put(AppConstants.DATA_MESSAGE_CHATFILES, allChatFiles);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	public ConversationMessageHistoryResponse messageToMessageResponse(Message message) {
		ConversationMessageHistoryResponse response = new ConversationMessageHistoryResponse();
		if (Objects.nonNull(message.getIsSeen()))
			response.setIsSeen(message.getIsSeen().toString());
		response.setChatFiles(message.getChatFiles());
		response.setConversationId(message.getConversationId());
		response.setMessage(message.getMessage());
		response.setId(message.getId());
//		if (Objects.nonNull(message.getRecieved()))
//			response.setRecieved(message.getRecieved().toString());
		response.setSentAt(message.getSentAt().toString());
		response.setSender(message.getSender().getId());
		if (Objects.nonNull(message.getRecipient()))
			response.setRecipient(message.getRecipient().getId());

		if (Objects.nonNull(message.getChatFiles()))
			response.setChatFiles(message.getChatFiles());

		return response;

	}

	public ResponseEntity<?> deleteMessage(Long messageId, String deleteMessageForEmail) {
		Message message = this.messageRepository.findById(messageId)
				.orElseThrow(() -> new UserNotFoundException(AppConstants.MESSAGE_NOT_FOUND));
		Map<String, Object> response = new HashMap<>();

		if (message.getSender().getEmail().equals(deleteMessageForEmail)) {
			message.setIsSenderDeleted(true);
		} else if (message.getRecipient().getEmail().equals(deleteMessageForEmail)) {
			message.setIsRecipientDeleted(true);

		}

		this.messageRepository.save(message);
		response.put(AppConstants.MESSAGE, AppConstants.MESSAGE_DELETED);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> updateMessageStatusToMarkedAsSeen(String conversationId,
			String userMessageEmailToMarkAsSeen) {
		Map<String, Object> response = new HashMap<>();
		this.messageRepository.updateIsSeenStatusForCurrentUser(conversationId, userMessageEmailToMarkAsSeen, LocalDateTime.now());
		response.put(AppConstants.MESSAGE, AppConstants.MESSAGE_MARKEDAS_SEEN);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
