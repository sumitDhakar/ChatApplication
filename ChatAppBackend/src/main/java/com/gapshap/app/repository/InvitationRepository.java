package com.gapshap.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gapshap.app.constants.InvitationStatus;
import com.gapshap.app.model.User;
import com.gapshap.app.model.chat.InvitationRequest;

@Repository
public interface InvitationRepository extends JpaRepository<InvitationRequest, Long> {

	public Optional<InvitationRequest> findBySenderAndRecipient(User sender, User recipient);

	public Page<InvitationRequest> findByRecipientAndRequestStatusOrderByCreatedAtDesc(User recipient,
			InvitationStatus status, Pageable page);

	@Query("SELECT COUNT(ir) > 0 FROM InvitationRequest ir WHERE ir.sender.email = :senderEmail AND ir.recipient.email = :recipientEmail")
	public Boolean existsBySenderAndRecipient(String senderEmail, String recipientEmail);

	public List<InvitationRequest> findAllByRecipient(User user, Sort sortBy);

	@Query("SELECT COUNT(ir) > 0 FROM InvitationRequest ir WHERE (ir.sender.email = :senderEmail AND ir.recipient.email = :recipientEmail) or (ir.sender.email = :recipient2 AND ir.recipient.email = :sender2)")
	public Boolean existsBySenderAndRecipientOrRecipientAndSender(String senderEmail, String recipientEmail, String recipient2,
			String sender2);
}
