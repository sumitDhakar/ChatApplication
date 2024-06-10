package com.gapshap.app.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gapshap.app.constants.AppConstants;
import com.gapshap.app.payload.GroupRequest;
import com.gapshap.app.service.IGroupService;

@RestController
@RequestMapping("/gapshap/groups")
@CrossOrigin("*")
public class GroupController {

	@Autowired
	private IGroupService groupService;
	@Autowired
	private ObjectMapper mapper;

//	@PostMapping("/createGroup", consumes = { "multipart/form-data", "application/octet-stream" })
	@PostMapping(path = "/createGroup", consumes = { "multipart/form-data", "application/octet-stream" })
	public ResponseEntity<?> createGroup(@RequestPart(value = "image", required = false) MultipartFile image,
			@RequestPart(name = "data") GroupRequest groupRequest, Principal p) {
//		
//		GroupRequest groupRequests = null;
//		try {
//			 groupRequests= mapper.readValue(groupRequest, GroupRequest.class);
//		} catch (JsonProcessingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		return groupService.createGroup(groupRequest, image, p.getName());
	}

	@PutMapping("/updateGroup")
	public ResponseEntity<?> updateGroup(MultipartFile image, String groupRequest, Principal p) {
		GroupRequest groupRequests = null;
		try {
			groupRequests = mapper.readValue(groupRequest, GroupRequest.class);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return groupService.updateGroup(groupRequests, image, p.getName());
	}

	@DeleteMapping("/deleteGroup/{id}")
	public ResponseEntity<?> deleteGroup(@PathVariable("id") Long groupId) {
		Boolean result = groupService.deleteGroup(groupId);
		if (result) {
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/getGroupById")
	public ResponseEntity<?> getGroupById(@RequestParam(name = "conversationId") String conversationId) {
		Map<String, Object> response = new HashMap<>();

		GroupRequest groupRequest = this.groupService
				.mapGroupChatToGroupRequest(this.groupService.getGroupChatByConversationId(conversationId));

		// Set success message and group data in the response map
		response.put(AppConstants.MESSAGE, AppConstants.GROUP_RETERIEVED_SUCCESS);
		response.put(AppConstants.DATA_MESSAGE, groupRequest);

		// Return the response as JSON
		return ResponseEntity.ok().body(response);

	}

	@GetMapping("getAllGroups")
	public ResponseEntity<?> getContactsOfUser(@RequestParam("forDestination") String destination, Principal p) {
		return this.groupService.getAllGroup(p.getName(), destination);

	}

}
