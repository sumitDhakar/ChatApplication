package com.gapshap.app.service.impl;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gapshap.app.constants.AppConstants;
import com.gapshap.app.exception.UserNotFoundException;
import com.gapshap.app.fileServicel.IFileService;
import com.gapshap.app.model.User;
import com.gapshap.app.model.UserRole;
import com.gapshap.app.payload.PageReqst;
import com.gapshap.app.payload.UserRequest;
import com.gapshap.app.payload.UserResponse;
import com.gapshap.app.payload.UserStatusResponse;
import com.gapshap.app.repository.UserRepository;
import com.gapshap.app.service.IUserService;

@Service
public class UserServiceImpl implements IUserService, UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ModelMapper mapper;

	@Autowired
	private IFileService fileSerive;
//	@Value("${profile.path}")
//	private String DIRECTORY;

	@Override
	public UserResponse userToSearchUserResponse(User user) {
		UserResponse response = new UserResponse();
		response.setId(user.getId());
		response.setProfileName(user.getProfileName());
		response.setLocation(user.getLocation());
		response.setUserName(user.getUserName());
		response.setBio(user.getBio());
		response.setId(user.getId());
		UserStatusResponse statusRes = new UserStatusResponse();
//		statusRes.setLastSeen(user.getUserStatus().getLastSeen().toString());
		statusRes.setIsOnline(user.getUserStatus().getIsOnline());
		statusRes.setId(user.getUserStatus().getId());
		response.setUserStatus(statusRes);
		return response;
	}

	@Override
	public UserResponse userToUserResponse(User user) {
		UserResponse response = new UserResponse();
		response.setId(user.getId());
		response.setProfileName(user.getProfileName());
		response.setUserName(user.getUserName());
		response.setBio(user.getBio());
		response.setId(user.getId());
		response.setPhoneNumber(user.getPhoneNumber());
		response.setEmail(user.getEmail());
		response.setLocation(user.getLocation());
		response.setAbout(user.getAbout());

		UserStatusResponse statusRes = new UserStatusResponse();
		if (Objects.nonNull(user.getUserStatus().getLastSeen()))
			statusRes.setLastSeen(user.getUserStatus().getLastSeen().toString());
		statusRes.setIsOnline(user.getUserStatus().getIsOnline());
		statusRes.setId(user.getUserStatus().getId());
		if (Objects.nonNull(user.getUserStatus().getLastSeen()))
			statusRes.setStatus(user.getUserStatus().getStatus().toString());
		response.setUserStatus(statusRes);
		return response;
	}

	@Override
	public ResponseEntity<?> getAllUsers(String email) {
		Map<String, Object> response = new HashMap<>();

		List<Object[]> userList = this.userRepository
				.findByIsActiveAndEmailNot(true,email);
		response.put(AppConstants.MESSAGE, AppConstants.USER_RETIREVED_SUCCESS);
		response.put(AppConstants.DATA_MESSAGE, userList);
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

	@Override
	public User getUserById(Long userId) {
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException(AppConstants.USER_NOT_FOUND_ID));
		System.err.println("user data"+user);
		return user;
	}

	@Override
	public ResponseEntity<?> getUserByEmailOrUserName(String value) {
		Map<String, Object> response = new HashMap<>();
		List<User> user = this.userRepository.findByEmailOrUserNameLike(value);
		response.put(AppConstants.MESSAGE, AppConstants.USER_RETERIEVED_SUCCESS);
		response.put(AppConstants.DATA_MESSAGE,
				user.stream().map(u -> this.userToUserResponse(u)).collect(Collectors.toList()));
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

	@Override
	public ResponseEntity<?> updateUser(UserRequest request, MultipartFile file, Principal p) {
		System.err.println(request);
		Map<String, Object> response = new HashMap<>();
		User user = this.userRepository.findByEmail(p.getName())
				.orElseThrow(() -> new UserNotFoundException(AppConstants.USER_NOT_FOUND_EMAIL));
		user.setBio(request.getBio());
//		user.setEmail(request.getEmail());
		user.setPhoneNumber(request.getPhoneNumber());
		user.setUserName(request.getUserName());
		user.setLocation(request.getLocation());
		user.setAbout(request.getAbout());
		if (Objects.nonNull(file)) {

			String uploadFileInFolde = this.fileSerive.uploadFileInFolder(file, "User");
			this.fileSerive.deleteImageFromCloudServer(user.getProfileName());
			user.setProfileName(uploadFileInFolde);
		}
		User save = this.userRepository.save(user);
		response.put(AppConstants.MESSAGE, AppConstants.USER_UPDATED_SUCCESS);
		response.put(AppConstants.DATA_MESSAGE, save);
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = this.userRepository.findByEmail(email)
				.orElseThrow(() -> new UserNotFoundException(AppConstants.USER_NOT_FOUND_EMAIL + email));
		Set<UserRole> userRoles = user.getUserRoles();
		List<SimpleGrantedAuthority> authorities = userRoles.stream()
				.map(ur -> new SimpleGrantedAuthority(ur.getRoles().getTitle())).collect(Collectors.toList());

		return new org.springframework.security.core.userdetails.User(email, user.getPassword(), authorities);
	}

	@Override
	public User getUserByEmail(String userEmail) {
		User user = this.userRepository.findByEmail(userEmail)
				.orElseThrow(() -> new UserNotFoundException(AppConstants.USER_NOT_FOUND_EMAIL + userEmail));
		return user;
	}

}
