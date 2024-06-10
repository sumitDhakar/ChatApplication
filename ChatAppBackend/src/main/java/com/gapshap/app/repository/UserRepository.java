package com.gapshap.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gapshap.app.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	public Optional<User> findByEmail(String email);

	@Query("SELECT	 u FROM User u WHERE u.email LIKE %:value% OR u.userName LIKE %:value%")
	public List<User> findByEmailOrUserNameLike(String value);

	
	@Query("SELECT	 u.email,u.profileName  FROM User u WHERE u.email != :email And u.isActive = :b")
	public List<Object[]> findByIsActiveAndEmailNot(boolean b, String email);

}
