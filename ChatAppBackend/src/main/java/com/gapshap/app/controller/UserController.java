package com.gapshap.app.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gapshap.app.constants.AppConstants;
import com.gapshap.app.payload.UserRequest;
import com.gapshap.app.service.IUserActiveStatusService;
import com.gapshap.app.service.IUserService;

@RestController
@RequestMapping("/gapshap/user")
@CrossOrigin("*")
public class UserController {

	@Autowired
	private IUserService userService;

	@Autowired
	private IUserActiveStatusService statusService;

	@GetMapping("/")
	public ResponseEntity<?> getAllUsers(Principal p) {
		return userService.getAllUsers(p.getName());
	}

	@GetMapping("/{userId}")
	public ResponseEntity<?> getUserById(@PathVariable Long userId) {
		Map<String, Object> response = new HashMap<>();
		response.put(AppConstants.MESSAGE, AppConstants.USER_RETERIEVED_SUCCESS);
		response.put(AppConstants.DATA_MESSAGE, this.userService.userToUserResponse(userService.getUserById(userId)));
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);

	}

	@GetMapping("/search")
	public ResponseEntity<?> getUserByEmailOrUserName(@RequestParam("value") String value) {
		return userService.getUserByEmailOrUserName(value);
	}

	@PutMapping(path = "/update", consumes = { "multipart/form-data", "application/octet-stream" })
	public ResponseEntity<?> updateUser(@RequestPart(value = "image", required = false) MultipartFile file,@RequestPart(name="userRequest") UserRequest userRequest, Principal p) {
//		System.err.println(file.getOriginalFilename());
		return userService.updateUser(userRequest,file, p);
	}

	@GetMapping("/status/{id}")
	public ResponseEntity<?> updateStatus(@PathVariable Long id) {
		return this.statusService.getUserStatus(id);
	}

	
	
	
	
//	@MessageMapping("/status")
//	public ResponseEntity<?> updateStatusOnline(@Payload UserStatusRequest request){
//		 UserStatus status = this.statusService.updateUserStatus(request);
//		this.messageTemplate.convertAndSend(AppConstants.STATUS_DESTINATION,request);
//		return  new ResponseEntity<>(request,HttpStatus.OK);
//	}
//	
//	@MessageMapping("/search")
//	public ResponseEntity<?> getUserByEmailOrName(@Payload String value){
//		ResponseEntity<?> response = this.userService.getUserByEmailOrUserName(value);
//		this.messageTemplate.convertAndSend("/user/open/search", response.getBody());
//		return response;
//	}
}
