package com.gapshap.app.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.gapshap.app.constants.AppConstants;
import com.gapshap.app.exception.ResourceAlreadyExists;
import com.gapshap.app.exception.ResourceNotFoundException;
import com.gapshap.app.model.User;
import com.gapshap.app.model.chat.Contacts;
import com.gapshap.app.payload.ContactRequest;
import com.gapshap.app.repository.ContactRepository;
import com.gapshap.app.service.IContactService;
import com.gapshap.app.service.IUserService;

@Service
public class ContactServiceImpl implements IContactService {

	@Autowired
	private ContactRepository contactRepository;


	@Autowired
	private IUserService userService;

	@Override
	public ResponseEntity<?> createContact(ContactRequest request) {
		Map<String, Object> response = new HashMap<>();

		User owner = this.userService.getUserByEmail(request.getOwner());
		User recipient = this.userService.getUserByEmail(request.getContact());
		if (this.contactRepository.existsByOwnerAndContact(owner.getId(), recipient.getId())) {
			throw new ResourceAlreadyExists(AppConstants.CONTACT_ALREADY_FOUND);
		}
		Contacts c1 = new Contacts();
		c1.setContact(recipient);
		c1.setOwner(owner);
		c1.setCreatedAt(LocalDateTime.now());

		Contacts c2 = new Contacts();
		c2.setContact(owner);
		c2.setOwner(recipient);
		c2.setCreatedAt(LocalDateTime.now());

		this.contactRepository.save(c1);
		this.contactRepository.save(c2);
		response.put(AppConstants.MESSAGE, AppConstants.CONTACT_RETRIEVED);
		response.put(AppConstants.DATA_MESSAGE, c1);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getContactOfOwner(String ownerEmail,String forDestination) {
		Map<String, Object> response = new HashMap<>();
		User user = this.userService.getUserByEmail(ownerEmail);

		if(forDestination.equals("CONTACTLIST"))
		{
			Map<String, List<User>> contactList = this.getContactsGroupedByFirstLetter(user.getId());
		response.put(AppConstants.DATA_MESSAGE, contactList);
		}
		else if(forDestination.equals("START_CONVERSATION"))
		{
			List<User> contactListForConversation = this.contactRepository.findContactOfGivenUserForConversation(user.getId(),false);
		response.put(AppConstants.DATA_MESSAGE, contactListForConversation);
		}

		response.put(AppConstants.MESSAGE, AppConstants.CONTACT_RETRIEVED);
		System.err.println("con"+response);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	


	public Map<String, List<User>> getContactsGroupedByFirstLetter(Long userID) {
		List<Contacts> contacts = this.contactRepository.findContactsByOwnerIdOrderByContactUserNameAsc(userID,false);

	
		Map<String, List<User>> groupedContacts = new TreeMap<>();

		// Iterate over contacts
		for (Contacts contact : contacts) {
			String firstLetter = contact.getContact().getUserName().substring(0, 1).toUpperCase();

			// If the map doesn't contain the letter, create a new list
			groupedContacts.putIfAbsent(firstLetter, new ArrayList<>());

			// Add user to the corresponding group
			groupedContacts.get(firstLetter).add(contact.getContact());
		}

		// Sort users within each group by username
		groupedContacts.forEach((letter, users) -> users.sort(Comparator.comparing(User::getUserName)));

		System.out.println(groupedContacts);
		return groupedContacts;
	}

	@Override
	public Optional<Contacts> findContactByOwnerAndContact(String owner, String recipiantEmail) {
		
		Optional<Contacts> contact = this.contactRepository.findByOwnerAndContact(owner,recipiantEmail);
		return contact;		
//		return null;
	}
	@Override
	public Boolean deleteContact(Long id,String currentUserEmail) {
		User userByEmail = this.userService.getUserByEmail(currentUserEmail);
		User user = this.userService.getUserById(id);
		Contacts contacts = this.contactRepository.findByContactAndOwner(user,userByEmail)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.CONTACT_NOT_FOUND+ id));
		contacts.setIsDeleted(true);
		this.contactRepository.save(contacts);
		return true;
	}

}
