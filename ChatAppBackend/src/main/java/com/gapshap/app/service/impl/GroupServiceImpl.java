package com.gapshap.app.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gapshap.app.constants.AppConstants;
import com.gapshap.app.exception.ResourceAlreadyExists;
import com.gapshap.app.exception.ResourceNotFoundException;
import com.gapshap.app.fileServicel.IFileService;
import com.gapshap.app.model.User;
import com.gapshap.app.model.chat.ChatGroupMembers;
import com.gapshap.app.model.chat.GroupChat;
import com.gapshap.app.payload.GroupRequest;
import com.gapshap.app.payload.GroupResponse;
import com.gapshap.app.repository.GroupRepository;
import com.gapshap.app.service.IGroupMemberChatService;
import com.gapshap.app.service.IGroupService;
import com.gapshap.app.service.IUserService;

@Service
public class GroupServiceImpl implements IGroupService {
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private IGroupMemberChatService iGroupMemberChatService;

	public GroupResponse groupToGroupResponse(GroupChat group) {
		return this.modelMapper.map(group, GroupResponse.class);
	}

	@Autowired
	private GroupRepository groupRepository;
	@Autowired
	private IFileService fileSerive;

	@Autowired
	private IUserService userService;

	public ResponseEntity<?> createGroup(GroupRequest groupRequest, MultipartFile file, String currentUser) {
		Map<String, Object> response = new HashMap<>();
		User owner = this.userService.getUserByEmail(currentUser);
		

		// Check if a group with the same name exists and is not marked as deleted
		if (this.groupRepository.existsByGroupNameAndIsDeleted(groupRequest.getGroupName(), false)) {
			throw new ResourceAlreadyExists(AppConstants.GROUP_ALREADY_EXIST + ":->" + groupRequest.getGroupName());
		}
		String random = UUID.randomUUID().toString();
		GroupChat newGroup = new GroupChat();
		newGroup.setConversationId(random);
		newGroup.setGroupName(groupRequest.getGroupName());
		newGroup.setDescription(groupRequest.getDescription());
		newGroup.setCreatedAt(LocalDateTime.now());
		newGroup.setIsDeleted(false);
		newGroup.setChatGroupMembers(null);

		if (Objects.nonNull(file)) {

			String uploadFileInFolde = this.fileSerive.uploadFileInFolder(file, "Group");
			newGroup.setProfileGroup(uploadFileInFolde);
		}

		// Save the group
		GroupChat savedGroup = groupRepository.save(newGroup);

		if (Objects.nonNull(groupRequest.getMembers())) {
			ChatGroupMembers cgm = new ChatGroupMembers();
			cgm.setIsLeader(true);
			cgm.setUserId(owner);
			groupRequest.getMembers().add(cgm);
			groupRequest.getMembers().forEach(member -> member.setGroupChatId(savedGroup));
			iGroupMemberChatService.addGroupMembers(groupRequest.getMembers());
		}

		// Build response
		response.put(AppConstants.GROUP_CHAT, AppConstants.GROUP_RETRIEVED);
		response.put(AppConstants.DATA_MESSAGE, savedGroup);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<?> updateGroup(GroupRequest groupRequest, MultipartFile file, String p) {
		Map<String, Object> response = new HashMap<>();

		// Fetch the group to be updated from the database
		GroupChat existingGroup = groupRepository.findById(groupRequest.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Group not found with id: " + groupRequest.getId()));

		// Update the group fields with the new values
		existingGroup.setDescription(groupRequest.getDescription());
		existingGroup.setGroupName(groupRequest.getGroupName());

		// Check if a new profile image file is provided
		if (Objects.nonNull(file)) {
			String uploadFileInFolder = this.fileSerive.uploadFileInFolder(file, "Group");
			existingGroup.setProfileGroup(uploadFileInFolder);
		}

		// Save the updated group
		GroupChat updatedGroup = groupRepository.save(existingGroup);

		// Build the response
		response.put(AppConstants.MESSAGE, AppConstants.GROUP_UPDATED_SUCCESS);
		response.put(AppConstants.DATA_MESSAGE, updatedGroup);

		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

	@Override
	public Boolean deleteGroup(Long groupId) {
		if (groupRepository.existsById(groupId)) {
			groupRepository.deleteById(groupId);
			return true;
		}
		return false;
	}

	@Override
	
	public ResponseEntity<?> getAllGroup(String ownerEmail, String forDestination) {
		Map<String, Object> response = new HashMap<>();
	

		if (ownerEmail == null || ownerEmail.isEmpty()) {
			response.put(AppConstants.MESSAGE, "Owner email cannot be empty");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		User user = userService.getUserByEmail(ownerEmail);
		if (user == null) {
			response.put(AppConstants.MESSAGE, "User not found");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		if (forDestination.equals("GROUPLIST")) {
			Map<String, List<GroupChat>> groupList = getGroupNames(user.getId());
			response.put(AppConstants.DATA_MESSAGE, groupList);
			response.put(AppConstants.MESSAGE, AppConstants.GROUP_RETRIEVED);
		} else if (forDestination.equals("START_CONVERSATION")) {
			// Assuming you want to handle conversation start logic here:
//            List<GroupChat> groupsForConversation = groupRepository.findByMembersEmailForConversation(ownerEmail);
//            response.put(AppConstants.DATA_MESSAGE, groupsForConversation);
		} else {
//            List<GroupChat> groups = groupRepository.findByMembersEmail(ownerEmail);
			List<GroupChat> groups = new ArrayList<>();
			response.put(AppConstants.DATA_MESSAGE, groups);
		}

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	public Map<String, List<GroupChat>> getGroupNames(Long userId) {
		List<GroupChat> groups = groupRepository.findGroupByUserId(userId);
		Map<String, List<GroupChat>> groupNames = new TreeMap<>();

		for (GroupChat group : groups) {
			String name = group.getGroupName();
			if (name != null && !name.isEmpty()) {
				String firstLetter = name.substring(0, 1).toUpperCase();
				groupNames.putIfAbsent(firstLetter, new ArrayList<>());
				groupNames.get(firstLetter).add(group);
			}
		}
		groupNames.forEach((letter, groupList) -> groupList.sort(Comparator.comparing(GroupChat::getGroupName)));
		return groupNames;
	}

	@Override
	public GroupChat getGroupChatByConversationId(String conversationId ) {
		
		    // Fetch group data from the service
		  GroupChat groupChat = this.groupRepository.findByConversationId(conversationId)
					.orElseThrow(() -> new ResourceNotFoundException(AppConstants.NO_GROUPS_FOUND));
		 
    return groupChat		;	
	}

	@Override
	public GroupRequest mapGroupChatToGroupRequest(GroupChat groupChat) {
		GroupRequest groupRequest = new GroupRequest();
		groupRequest.setId(groupChat.getId());
		groupRequest.setGroupName(groupChat.getGroupName());
		groupRequest.setMembers(groupChat.getChatGroupMembers());
//        groupRequest.setMessages(groupChat.getMessages());
//		groupRequest.setCreatedAt(groupChat.getCreatedAt());
		groupRequest.setDescription(groupChat.getDescription());

		groupRequest.setProfileGroup(groupChat.getProfileGroup());
		return groupRequest;
	}

}
