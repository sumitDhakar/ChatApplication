package com.gapshap.app.payload;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactRequest {
	
	private String owner;
	
	private String contact; 
}
