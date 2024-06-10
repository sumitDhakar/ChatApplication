package com.gapshap.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gapshap.app.model.chat.GroupChat;

public interface GroupRepository extends JpaRepository<GroupChat, Long> {

	public boolean existsByGroupNameAndIsDeleted(String name, boolean b);

	public Optional<GroupChat> findByConversationId(String conversationId);

	 @Query(value = "SELECT gc.* " +
             "FROM group_chat gc " +
             "INNER JOIN chat_group_members cgm ON gc.id = cgm.group_id " +
             "WHERE cgm.user_id = :userId", nativeQuery = true)
	public List<GroupChat> findGroupByUserId(Long userId);

//	
//	List<GroupChat> findGorupChatByNameOrderByName(Long id,boolean b);

//	List<GroupChat> findGorupChatByNameOrderByName(Long userID, boolean b);
//	 List<GroupChat> findIsDeletedOrderByName(Long ownerId, boolean isDeleted);

//	List<GroupChat> findGroupChat_idOrderByNameAsc(Long userID, boolean b);

//	List<GroupChat> findByMembers_IdAndIsDeletedOrderBynameAsc(Long userID, boolean b);
//	   List<GroupChat> findByMembers_IdAndIsDeletedOrderBynameAsc(Long userId, boolean isDeleted);
//	  List<GroupChat> findByMembers_IdAndIsDeletedOrderByCreatedAtAsc(Long userId, boolean isDeleted);

}
