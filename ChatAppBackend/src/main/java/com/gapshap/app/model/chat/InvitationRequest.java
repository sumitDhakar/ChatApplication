package com.gapshap.app.model.chat;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.gapshap.app.constants.InvitationStatus;
import com.gapshap.app.model.User;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class InvitationRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "sender_id")
	@JsonIgnoreProperties(value = {"createdAt","updateAt","isActive","userRoles","contacts"})
	private User sender;

	@ManyToOne
	@JoinColumn(name = "recipient_id")
	@JsonIgnoreProperties(value = {"createdAt","updateAt","isActive","userRoles","contacts",""})
	private User recipient;

	private Boolean isAccepted = false;

	@Enumerated(EnumType.STRING)
	private InvitationStatus requestStatus;
	private String invitationMessage="Be My Friend 😊😊";
//	@JsonIgnore
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
	private LocalDateTime createdAt;

}
