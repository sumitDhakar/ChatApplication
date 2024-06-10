package com.gapshap.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gapshap.app.model.chat.Conversation;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long>{

	@Query("SELECT c FROM Conversation c WHERE  c.sender.id=:sender AND c.recipient.id=:recipient")
	Optional<Conversation> findBySenderAndRecipient(Long sender,Long recipient);
	
	@Query("SELECT c FROM Conversation c WHERE c.sender.id = :userId ORDER BY c.lastMessageAt DESC")
	List<Conversation>  getAllConversationsOfGivenUserId(Long userId);

	List<Conversation> findByConversationId(String conversationId);
	
}
