package com.gapshap.app.model.chat;

import java.time.LocalDateTime;

import org.hibernate.annotations.ManyToAny;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gapshap.app.model.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Notifications {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "sender_id")
	@JsonIgnoreProperties(value = {"createdAt","updateAt","isActive","userRoles","contacts",""})
	private User sender;
	
	@ManyToOne
	@JoinColumn(name = "recipient_id")
	@JsonIgnoreProperties(value = {"createdAt","updateAt","isActive","userRoles","contacts",""})
	private User recipient;
	
	private String message;
	
	private LocalDateTime time;
	
	private Boolean isSeen=false;
	
	private Boolean isClear=false;
}
