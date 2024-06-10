package com.gapshap.app.payload;

import java.util.List;
import java.util.Map;

import com.gapshap.app.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactDTO {

	private  Map<String, List<User>> groupedContacts;
	
}
