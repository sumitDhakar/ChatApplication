package com.gapshap.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gapshap.app.model.User;
import com.gapshap.app.model.chat.Contacts;

@Repository
public interface ContactRepository extends JpaRepository<Contacts, Long> {

	@Query("SELECT c FROM Contacts c WHERE c.owner.email=:ownerEmail AND c.contact.email=:recipientEmail")
	public Optional<Contacts> findByOwnerAndContact(String ownerEmail, String recipientEmail);

	@Query("SELECT c FROM Contacts c WHERE c.owner.email=:email")
	public List<Contacts> findByOwner(String email);

	@Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Contacts c WHERE (c.owner.id = :owner1 AND c.contact.id = :contact1) ")
	public boolean existsByOwnerAndContact(Long owner1, Long contact1);

	@Query("SELECT c FROM Contacts c WHERE c.owner.id = :ownerId and c.isDeleted = :deleted ORDER BY c.contact.userName ASC")
	public List<Contacts> findContactsByOwnerIdOrderByContactUserNameAsc(Long ownerId,Boolean deleted);

	
	@Query("SELECT c.contact FROM Contacts c WHERE c.owner.id = :ownerId and c.isDeleted = :deleted ORDER BY c.contact.userName ASC")
	public List<User> findContactOfGivenUserForConversation(Long ownerId,Boolean deleted );

	public Optional<Contacts> findByContact(User user);

	public Optional<Contacts> findByContactAndOwner(User user, User userByEmail);

	
}
