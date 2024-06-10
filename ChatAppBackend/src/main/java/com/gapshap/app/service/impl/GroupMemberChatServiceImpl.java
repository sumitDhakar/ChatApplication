package com.gapshap.app.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.gapshap.app.constants.AppConstants;
import com.gapshap.app.exception.ResourceNotFoundException;
import com.gapshap.app.model.User;
import com.gapshap.app.model.chat.ChatGroupMembers;
import com.gapshap.app.model.chat.GroupChat;
import com.gapshap.app.repository.ChatGroupMemberRepository;
import com.gapshap.app.service.IGroupMemberChatService;

@Service
public class GroupMemberChatServiceImpl implements IGroupMemberChatService {

	@Autowired
	private ChatGroupMemberRepository chatGroupMemberRepository;

	@Override
	public ResponseEntity<?> addGroupMembers(List<ChatGroupMembers> memberRequestList) {
		Map<String, Object> response = new HashMap<>();
		List<ChatGroupMembers> groupMembers = new ArrayList<>();
		for (ChatGroupMembers l : memberRequestList) {
			GroupChat gc = new GroupChat();
			gc.setId(l.getGroupChatId().getId());
			User gMember = new User();
			gMember.setId(l.getUserId().getId());
			Optional<ChatGroupMembers> memb = this.chatGroupMemberRepository.findByGroupChatIdAndUserId(gc, gMember);
			if (memb.isEmpty())
				groupMembers.add(l);
		}
		groupMembers = this.chatGroupMemberRepository.saveAll(groupMembers);
		response.put(AppConstants.MESSAGE, AppConstants.GROUPMEMBER_CREATED);
		response.put(AppConstants.DATA_MESSAGE, groupMembers);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<?> updateGroupMembers(ChatGroupMembers memberRequest) {
		Map<String, Object> response = new HashMap<>();
		ChatGroupMembers member = this.chatGroupMemberRepository.findById(memberRequest.getId()).orElseThrow(
				() -> new ResourceNotFoundException(AppConstants.NO_GROUPMEMBER_FOUND + memberRequest.getId()));
		member.setIsLeader(memberRequest.getIsLeader());
		member.setGroupChatId(memberRequest.getGroupChatId());
		member.setUserId(memberRequest.getUserId());
		ChatGroupMembers save = this.chatGroupMemberRepository.save(member);

		response.put(AppConstants.MESSAGE, AppConstants.GROUPMEMBER_UPDATE);
		response.put(AppConstants.DATA_MESSAGE, save);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<?> getGroupMembersById(Long id) {
		Map<String, Object> response = new HashMap<>();
		ChatGroupMembers member = this.chatGroupMemberRepository.findByIdAndDeleted(id, false)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.NO_GROUPMEMBER_FOUND + id));
		response.put(AppConstants.DATA_MESSAGE, member);
		response.put(AppConstants.MESSAGE, AppConstants.GROUPMEMBER_RETERIEVED_SUCCESS);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getGroupMembersByGroupId(Long id) {
		Map<String, Object> response = new HashMap<>();
		GroupChat gc = new GroupChat();
		gc.setId(id);
		List<ChatGroupMembers> members = this.chatGroupMemberRepository.findByGroupChatIdAndDeleted(gc, false);

		response.put(AppConstants.DATA_MESSAGE, members);
		response.put(AppConstants.MESSAGE, AppConstants.GROUPMEMBER_RETERIEVED_SUCCESS);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getGroupMembersByUserId(Long id) {
		Map<String, Object> response = new HashMap<>();
		response.put(AppConstants.MESSAGE, AppConstants.GROUPMEMBER_RETERIEVED_SUCCESS);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getAllGroupMembers() {
		Map<String, Object> response = new HashMap<>();
		response.put(AppConstants.MESSAGE, AppConstants.GROUPMEMBER_RETERIEVED_SUCCESS);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> deleteGroupMembers(Long id) {
		Map<String, Object> response = new HashMap<>();
		ChatGroupMembers member = this.chatGroupMemberRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.NO_GROUPMEMBER_FOUND + id));
		member.setDeleted(true);
		this.chatGroupMemberRepository.save(member);
		response.put(AppConstants.MESSAGE, AppConstants.GROUPMEMBER_RETERIEVED_SUCCESS);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> makeLeader(ChatGroupMembers memberRequest) {
		Map<String, Object> response = new HashMap<>();
		GroupChat gc = new GroupChat();
		gc.setId(memberRequest.getGroupChatId().getId());
		User employee = new User();
		employee.setId(memberRequest.getUserId().getId());
		ChatGroupMembers gcMember = this.chatGroupMemberRepository.findByGroupChatIdAndUserId(gc, employee)
				.orElseThrow(() -> new ResourceNotFoundException(
						AppConstants.NO_GROUPMEMBER_FOUND + memberRequest.getGroupChatId().getId() + " and user id :"
								+ memberRequest.getUserId().getId()));
		gcMember.setIsLeader(memberRequest.getIsLeader());
		gcMember = this.chatGroupMemberRepository.save(gcMember);

		response.put(AppConstants.MESSAGE, AppConstants.GROUPMEMBER_RETERIEVED_SUCCESS);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
