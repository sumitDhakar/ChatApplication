package com.gapshap.app.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gapshap.app.service.IContactService;

@RestController
@RequestMapping("/gapshap/contacts/")
@CrossOrigin("*")
public class ContactController {

	@Autowired
	private IContactService contactService;

	@GetMapping("allContacts")
	public ResponseEntity<?> getContactsOfUser(@RequestParam("forDestination") String destination,Principal p) {
		return this.contactService.getContactOfOwner(p.getName(),destination);

	}
	
	@DeleteMapping("deleteContact")
	public ResponseEntity<Boolean> deletedDepartment(@RequestParam("contactId") Long contactsId,Principal p ){
		
		return ResponseEntity.ok(contactService.deleteContact(contactsId,p.getName()));
	}
	
}
