package com.gapshap.app.controller;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gapshap.app.fileServicel.IFileService;
import com.gapshap.app.payload.LoginRequest;
import com.gapshap.app.payload.ResendOtpRequest;
import com.gapshap.app.payload.UserRegistrationRequest;
import com.gapshap.app.payload.VerificationRequest;
import com.gapshap.app.service.IAuthenticationService;

@RestController
@RequestMapping("/gapshap/auth")
@CrossOrigin("*")
public class AuthenticationController {

	@Autowired
	private IAuthenticationService authService;

	@Autowired
	private IFileService fileService;

	@PostMapping("/")
	public ResponseEntity<?> registration(@RequestBody UserRegistrationRequest request) {
		return authService.registration(request);
	}

	@PostMapping("/verify")
	public ResponseEntity<?> verifyOtp(@RequestBody VerificationRequest request) {
		return authService.verifyUser(request);
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest request) {
		return authService.loginUser(request);
	}

	@GetMapping("/current-user")
	public ResponseEntity<?> getCurrentUser(Principal p) {
		return authService.getCurrentUser(p);
	}

	@PostMapping("/resend")
	public ResponseEntity<?> resendOtp(@RequestBody ResendOtpRequest resendOtpRequest) {
		return authService.resendOtp(resendOtpRequest);
	}

//	@GetMapping(value = "/profileImage")
//	public ResponseEntity<?> getProfileImg( @RequestPart(name = "imageName") String imageName) {
//		
//			InputStream imageFile = this.fileService.downloadImageFromCloud(imageName);
//			System.err.println(imageFile.toString());
////			StreamUtils.copy(imageFile, response.getOutputStream());
//			if(Objects.nonNull(imageFile)||imageFile!=null)
//			 return new ResponseEntity<>(imageFile, HttpStatus.OK);
//			  return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//		
//	}

	@GetMapping(value = "/profileImage")
	public ResponseEntity<?> getProfileImg(@RequestParam(name = "imageName") String imageName) {
		byte[] imageStream = this.fileService.downloadImageFromCloud(imageName);
		if (imageStream != null) {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_JPEG); // Adjust content type based on image format
			headers.setContentDispositionFormData("attachment", imageName); // Optional: Set download filename
			return new ResponseEntity<>(imageStream, headers, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Image not found", HttpStatus.NOT_FOUND);
		}

	}

}
