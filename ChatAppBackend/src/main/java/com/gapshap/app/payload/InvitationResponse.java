package com.gapshap.app.payload;

import java.time.LocalDateTime;

import com.gapshap.app.constants.InvitationStatus;
import com.gapshap.app.model.User;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvitationResponse {

	private Long id;

	private User sender;

	private InvitationStatus requestStatus;

	private LocalDateTime createdAt;

}
