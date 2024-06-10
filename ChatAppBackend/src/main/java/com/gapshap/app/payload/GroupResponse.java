package com.gapshap.app.payload;

import java.time.LocalDateTime;
import java.util.List;

import com.gapshap.app.constants.InvitationStatus;
import com.gapshap.app.model.User;
import com.gapshap.app.model.chat.Message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupResponse {
	private Long id;

	private String name;

	private List<User> members;

	private List<Message> messages;

	private LocalDateTime createdAt;

	private String description;

	private Boolean isDeleted = false;

	private String profileGroup = "https://res.cloudinary.com/dizz5tuug/image/upload/v1706875603/DEFAULT_USER/guest-user_g42o3j.png";

}
