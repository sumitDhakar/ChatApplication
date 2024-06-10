package com.gapshap.app.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.gapshap.app.model.chat.GroupChat;
import com.gapshap.app.payload.GroupRequest;

public interface IGroupService {

	ResponseEntity<?> createGroup(GroupRequest groupRequest, MultipartFile file, String p);

	ResponseEntity<?> updateGroup(GroupRequest group, MultipartFile file, String p);

	public Boolean deleteGroup(Long groupId);

	public ResponseEntity<?> getAllGroup(String ownerEmail, String forDestination);

	public GroupChat getGroupChatByConversationId(String  conversationId);
	
	public GroupRequest mapGroupChatToGroupRequest(GroupChat groupChat);
}
