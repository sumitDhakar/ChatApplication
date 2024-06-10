package com.gapshap.app.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.gapshap.app.model.chat.Contacts;
import com.gapshap.app.model.chat.UserActiveStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String userName;
	
	private String email;
	
	private String password;
	
	private String location="Enter you location";
	
	private String phoneNumber="Enter you phoneNumber";
	
	private String about;
	
	private String bio;
	
	private String profileName="https://res.cloudinary.com/dizz5tuug/image/upload/v1706875603/DEFAULT_USER/guest-user_g42o3j.png";
	
	private Boolean isActive=true;
	 @JsonSerialize(using = LocalDateTimeSerializer.class)
	  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
	  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
	private LocalDateTime createdAt;
	 @JsonSerialize(using = LocalDateTimeSerializer.class)
	  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
	  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
	 private LocalDateTime updateAt;
	 
	 @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	 @JoinColumn(name="u_id")
	 private Set<UserRole> userRoles;
	 
	 @OneToOne(cascade = CascadeType.ALL)
	 private UserActiveStatus  userStatus;
	 
	 @OneToMany(fetch = FetchType.EAGER)
	 @JoinColumn(name="owner_id")
	 @JsonIgnore
	 public List<Contacts> contacts;
}
