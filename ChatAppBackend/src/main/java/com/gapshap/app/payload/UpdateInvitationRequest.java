package com.gapshap.app.payload;

import com.gapshap.app.constants.InvitationStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateInvitationRequest {

	private Long id;
	
	private InvitationStatus requestStatus;
}
