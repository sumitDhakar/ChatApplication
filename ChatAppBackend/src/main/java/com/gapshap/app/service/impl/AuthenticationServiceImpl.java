package com.gapshap.app.service.impl;

import java.security.Principal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.gapshap.app.constants.AppConstants;
import com.gapshap.app.constants.EmailStatus;
import com.gapshap.app.exception.ResourceAlreadyExists;
import com.gapshap.app.exception.UserNotFoundException;
import com.gapshap.app.model.Roles;
import com.gapshap.app.model.User;
import com.gapshap.app.model.UserRegistration;
import com.gapshap.app.model.UserRole;
import com.gapshap.app.model.chat.UserActiveStatus;
import com.gapshap.app.payload.LoginRequest;
import com.gapshap.app.payload.ResendOtpRequest;
import com.gapshap.app.payload.UserRegistrationRequest;
import com.gapshap.app.payload.UserResponse;
import com.gapshap.app.payload.VerificationRequest;
import com.gapshap.app.repository.UserRegistrationRepository;
import com.gapshap.app.repository.UserRepository;
import com.gapshap.app.security.utils.JWTUtils;
import com.gapshap.app.service.IAuthenticationService;
import com.gapshap.app.service.IUserService;
import com.gapshap.app.utils.EmailUtils;
import static com.gapshap.app.constants.AppConstants.MESSAGE;

@Service
public class AuthenticationServiceImpl implements IAuthenticationService {

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private EmailUtils emailService;

	@Autowired
	private UserRegistrationRepository userRegistraionRepository;

	@Autowired
	private UserRepository userRepository;
	

	@Autowired
	private IUserService userService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JWTUtils jwtUtils;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	public ResponseEntity<?> registration(UserRegistrationRequest request) {
		Map<String, Object> response = new HashMap<>();

		UserRegistration userRegistration;

		Optional<UserRegistration> user = this.userRegistraionRepository.findByEmail(request.getEmail());

		// checking if user account is already present
		if (user.isPresent() && user.get().getIsActive()) {
			response.put(MESSAGE, AppConstants.USER_ALREADY_REGISTERED_WITH_EMAIL);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		// checking if user account is already present and is verified or is active
		if (user.isPresent() && !user.get().getIsActive())
			userRegistration = user.get();

		else {
			userRegistration = new UserRegistration();
			userRegistration.setCreatedAt(LocalDateTime.now());
		}

		String otp = otpGenerator().toString();

		userRegistration.setEmail(request.getEmail());
		userRegistration.setUserName(request.getUserName());
		userRegistration.setPassword(passwordEncoder.encode(request.getPassword()));
		userRegistration.setOtp(otp);
		userRegistration.setUpdatedAt(LocalDateTime.now());

		// sending email for verification
		Boolean isSentSuccessfully = this.emailService.sendEmail(request.getEmail(), request.getUserName(), otp);

		if (isSentSuccessfully) {

			response.put(MESSAGE, AppConstants.USER_REGISTRATION_SUCCESS);
			response.put(AppConstants.EMAIL_STATUS, AppConstants.VERIFICATION_EMAIL_SEND);
			response.put(AppConstants.EMAIL, request.getEmail());
			Optional<User> userAccount = this.userRepository.findByEmail(userRegistration.getEmail());

			if (userAccount.isPresent())
				throw new ResourceAlreadyExists(
						AppConstants.USER_ALREADY_REGISTERED_WITH_EMAIL + userAccount.get().getEmail());

			this.userRegistraionRepository.save(userRegistration);

		} else {
			response.put(MESSAGE, AppConstants.USER_REGISTRATION_FAILED);
			response.put(AppConstants.EMAIL_STATUS, AppConstants.EMAIL_SEND_STATUS_FAILED);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<?> verifyUser(VerificationRequest request) {
		Optional<UserRegistration> userRegistered = this.userRegistraionRepository.findByEmailAndOtp(request.getEmail(),
				request.getOtp());
		Map<String, Object> response = new HashMap<>();
		if (userRegistered.isEmpty()) {
			response.put(MESSAGE, AppConstants.INVALID_OTP);
			return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
		}

		if (userRegistered.get().getIsActive()) {
			response.put(MESSAGE, AppConstants.USER_ALREADY_REGISTERED_WITH_EMAIL);
			return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
		}

		if (TimeUnit.SECONDS.toMinutes(Duration.between(userRegistered.get().getUpdatedAt(), LocalDateTime.now())
				.getSeconds()) > AppConstants.LINK_EXPIRATION_TIME) {
			response.put(MESSAGE, AppConstants.LINK_EXPIRED);
			return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
		}
		User user = new User();
		user.setCreatedAt(LocalDateTime.now());
		user.setUpdateAt(user.getCreatedAt());
		user.setEmail(userRegistered.get().getEmail());
		user.setPassword(userRegistered.get().getPassword());
		user.setUserName(userRegistered.get().getUserName());

		Roles role = new Roles();
		role.setId(AppConstants.USER_ROLE_ID);

		UserRole ur = new UserRole();
		ur.setRoles(role);
		ur.setUser(user);
		Set<UserRole> userRoles = new HashSet<>();
		userRoles.add(ur);
		user.setUserRoles(userRoles);
		userRegistered.get().setIsActive(true);

		UserActiveStatus userStatus = new UserActiveStatus();
		userStatus.setIsOnline(false);
		user.setUserStatus(userStatus);

		this.userRepository.save(user);
		this.userRegistraionRepository.save(userRegistered.get());
		response.put(MESSAGE, AppConstants.USER_VERIFICATION_SUCCESS);
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

	@Override
	public ResponseEntity<?> loginUser(LoginRequest loginRequest) {
		this.authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
		String token = this.jwtUtils.generateToken(loginRequest.getEmail());
		Map<String, Object> response = new HashMap<>();

		response.put(MESSAGE, AppConstants.TOKEN_GENERATED);
		response.put(AppConstants.TOKEN, token);

		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

	@Override
	public ResponseEntity<?> getCurrentUser(Principal p) {
		Map<String, Object> response = new HashMap<>();

		if (Objects.isNull(p)) {
			response.put(MESSAGE, AppConstants.INVALID_USER);
			return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
		}

//		User user = this.userRepository.findByEmail(p.getName())
//				.orElseThrow(() -> new UserNotFoundException(AppConstants.USER_NOT_FOUND));
		User user = this.userService.getUserByEmail(p.getName());
//		UserResponse userResponse = new UserResponse();
//		userResponse.setId(user.getId());
//		userResponse.setEmail(user.getEmail());
//		userResponse.setLocation(user.getLocation());
//		userResponse.setAbout(user.getAbout());
//		userResponse.setPhoneNumber(user.getPhoneNumber());
//		userResponse.setProfileName(user.getProfileName());
//		userResponse.setUserName(user.getUserName());
//		userResponse.setUserRoles(user.getUserRoles());
		response.put(MESSAGE, AppConstants.USER_RETERIEVED_SUCCESS);

		response.put(AppConstants.DATA_MESSAGE, user);
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

	@Override
	public ResponseEntity<?> resendOtp(ResendOtpRequest request) {

		Optional<UserRegistration> user = this.userRegistraionRepository.findByEmail(request.getEmail());

		Map<String, Object> response = new HashMap<>();

		if (user.isEmpty()) {
			response.put(MESSAGE, AppConstants.USER_REGISTRATION_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		if (user.isPresent() && user.get().getIsActive()) {
			response.put(MESSAGE, AppConstants.USER_ALREADY_REGISTERED_WITH_EMAIL);
			return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
		}
		String otp = this.otpGenerator().toString();
		UserRegistration userRegistration = user.get();
		userRegistration.setOtp(otp);
		userRegistration.setUpdatedAt(LocalDateTime.now());

		Boolean isSentSuccessfully = this.emailService.sendEmail(request.getEmail(), user.get().getUserName(), otp);

		if (isSentSuccessfully) {

			response.put(MESSAGE, AppConstants.OTP_SENT_SUCCESS);
			response.put(AppConstants.EMAIL_STATUS, AppConstants.VERIFICATION_EMAIL_SEND);
			response.put(AppConstants.EMAIL, request.getEmail());
			this.userRegistraionRepository.save(userRegistration);
		} else {
			response.put(MESSAGE, AppConstants.USER_REGISTRATION_FAILED);
			response.put(AppConstants.EMAIL_STATUS, AppConstants.EMAIL_SEND_STATUS_FAILED);
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	private Integer otpGenerator() {
		return new Random().nextInt(999999);
	}

}
