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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChatGroupMembers {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="group_id")
	@JsonIgnoreProperties(value={"chatGroupMembers","profileGroup","isDeleted","createdAt","description"})
	private GroupChat groupChatId;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	@JsonIgnoreProperties(value = {"createdAt","updateAt","isActive","userRoles","contacts",""})
	private User userId;
	
	private Boolean isLeader;
	 
	private Boolean deleted=false;

}
