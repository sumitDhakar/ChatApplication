package com.gapshap.app.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gapshap.app.model.chat.Message;

import jakarta.transaction.Transactional;

public interface MessageRepository extends JpaRepository<Message, Long>{

	@Query("SELECT m FROM Message m WHERE m.sender.email=:sender AND m.recipient.email=:recipient ORDER BY m.sentAt DESC")
	public List<Message> findBySenderAndRecipient(String sender,String recipient);

	public List<Message> findByConversationIdOrderBySentAt(String conversationId);
	
	@Query("SELECT m FROM Message m WHERE m.conversationId = :conversationId " +
	           "AND ((m.sender.email = :currentUser AND m.isSenderDeleted = false) OR " +
	           "(m.recipient.email = :currentUser AND m.isRecipientDeleted = false))"+
	           "ORDER BY m.sentAt ASC")
	public List<Message> findByConversationIdOrderBySentAtForSingleUser(String conversationId,String currentUser);
//	 @Query("SELECT m FROM Message m WHERE m.conversationId = :conversationId AND m.isDeleted = false " +
//	           "ORDER BY m.sentAt ASC")
//	    List<Message> findAllByConversationIdOrderBySentAtDescAndCurrentUserNotDeleted(
//	            @Param("conversationId") String conversationId,
//	            @Param("currentUserDeleted") String currentUserDeleted
//	    );
	
	
	
	
	 @Query("SELECT COUNT(m) FROM Message m " +
	           "WHERE m.isSeen = false " +
	           "AND m.conversationId = :conversationId " +
	           "AND m.sender.email != :currentUser " +
	           "AND ((m.sender.email = :currentUser AND m.isSenderDeleted = false) " +
	           "OR (m.recipient.email = :currentUser AND m.isRecipientDeleted = false))")
	 public    Long countUnseenMessagesByConversationIdAndCurrentUser(
	        @Param("conversationId") String conversationId,
	        @Param("currentUser") String currentUser
	    );
	 
	 @Modifying
	    @Transactional
	    @Query("UPDATE Message m SET m.isSeen = true " +
	           "WHERE m.conversationId = :conversationId " +
	           "AND m.recipient.email = :currentUser " +
	           "AND m.isSeen = false " +
	           "AND m.sentAt < :currentTime")
	    void updateIsSeenStatusForCurrentUser(
	        @Param("conversationId") String conversationId,
	        @Param("currentUser") String currentUser,
	        @Param("currentTime") LocalDateTime currentTime
	    );
	 
	
	
}
