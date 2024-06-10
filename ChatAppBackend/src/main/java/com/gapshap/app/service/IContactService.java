package com.gapshap.app.service;

import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.gapshap.app.model.chat.Contacts;
import com.gapshap.app.payload.ContactRequest;

public interface IContactService {
	public ResponseEntity<?> createContact(ContactRequest request);

	public ResponseEntity<?> getContactOfOwner(String ownerEmail, String forDestination);

	public Optional<Contacts> findContactByOwnerAndContact(String owner, String recipiantEmail);

	public Boolean deleteContact(Long id,String currentUserEmail);
}
