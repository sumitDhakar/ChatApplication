package com.gapshap.app.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
	
	private Long   id;

	private String userName;

	private String email;

	private String password;
	private String location = "Enter you location";

	private String phoneNumber;

	private String about;
	private String bio;

//	private MultipartFile profileNamedata;
}
