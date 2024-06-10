package com.gapshap.app.model.chat;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gapshap.app.model.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne()
	@JoinColumn(name = "sender_id")
	@JsonIgnoreProperties(value = {"createdAt","updateAt","isActive","userRoles","contacts",""})
	private User sender;

	@ManyToOne
	@JoinColumn(name = "recipient_id")
	@JsonIgnoreProperties(value = {"createdAt","updateAt","isActive","userRoles","contacts",""})
	private User recipient;

	private String message;

	private String conversationId;

	private Boolean isSeen=false;

	private Boolean isDeleted = false;
	
//	
//	@ManyToOne
//	private GroupChat groupChat;

	private LocalDateTime sentAt;
	private MessageType type;
//	private LocalDateTime recieved;

	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	@JoinColumn(name = "message_id")
	@JsonIgnoreProperties(value = { "message" })
	private List<ChatFiles> chatFiles;
//
	private Boolean isSenderDeleted = false;
	private Boolean isRecipientDeleted = false;
//
	

}
