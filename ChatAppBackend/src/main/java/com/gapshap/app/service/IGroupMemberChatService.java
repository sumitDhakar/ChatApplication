package com.gapshap.app.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.gapshap.app.model.chat.ChatGroupMembers;

public interface IGroupMemberChatService {

	
public ResponseEntity<?> addGroupMembers(List<ChatGroupMembers> memberRequest);
	
	public ResponseEntity<?> updateGroupMembers(ChatGroupMembers memberRequest);
	
	public ResponseEntity<?> getGroupMembersById(Long id);
	
	public ResponseEntity<?> getGroupMembersByGroupId(Long id);
	
	public ResponseEntity<?> getGroupMembersByUserId(Long id);
	
	public ResponseEntity<?> getAllGroupMembers();
	
	public ResponseEntity<?> deleteGroupMembers(Long id);
	
	public ResponseEntity<?> makeLeader(ChatGroupMembers memberRequest);
}
