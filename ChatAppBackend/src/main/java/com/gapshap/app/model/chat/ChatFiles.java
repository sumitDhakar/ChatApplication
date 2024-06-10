package com.gapshap.app.model.chat;

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
public class ChatFiles {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String fileName;

	private String serverFileName;

	private Long fileSize;
	
//	@ManyToOne
//	@JoinColumn(name = "sender_id")
//	@JsonIgnoreProperties(value = {"createdAt","updateAt","isActive","userRoles","contacts",""})
//
//	private User uploadedBy;

	@ManyToOne
	@JoinColumn(name = "message_id")
	@JsonIgnoreProperties(value = { "chatFiles","isRecipientDeleted","isSenderDeleted","type","sentAt","isDeleted","sender","conversationId","message","isSeen","recipient" })
	private Message message;

	private Boolean isDeleted = false;

}
