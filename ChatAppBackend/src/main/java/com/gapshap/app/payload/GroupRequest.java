package com.gapshap.app.payload;

import java.util.List;

import com.gapshap.app.model.chat.ChatGroupMembers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupRequest {
	private Long id;

	private String groupName;

	private List<ChatGroupMembers> members;
	private String profileGroup ;
	private String description;

	private Boolean isDeleted = false;


}
