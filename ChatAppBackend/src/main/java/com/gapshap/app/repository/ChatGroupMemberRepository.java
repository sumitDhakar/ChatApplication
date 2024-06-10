package com.gapshap.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gapshap.app.model.User;
import com.gapshap.app.model.chat.ChatGroupMembers;
import com.gapshap.app.model.chat.GroupChat;

public interface ChatGroupMemberRepository extends JpaRepository<ChatGroupMembers, Long> {

	public List<ChatGroupMembers> findByGroupChatIdAndDeleted(GroupChat groupChatId, Boolean condition);

	public List<ChatGroupMembers> findByUserId(User userId);

	public Optional<ChatGroupMembers> findByGroupChatIdAndUserId(GroupChat groupChatId, User userId);

	public Optional<ChatGroupMembers> findByIdAndDeleted(Long id, boolean b);

//    @Query(value ="select  p.id  , p.title from `projects` p where id in (Select pm.project_id_id from `project_members` pm where pm.user_id_id=1)",nativeQuery =true) 
//    public List<Object[]> findBYUserId(Integer userId);

}
