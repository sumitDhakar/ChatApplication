package com.gapshap.app.payload;

import com.gapshap.app.constants.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStatusRequest {
private String email;
private String status;
}
