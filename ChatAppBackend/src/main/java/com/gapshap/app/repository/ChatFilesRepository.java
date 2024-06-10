package com.gapshap.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gapshap.app.model.chat.ChatFiles;

public interface ChatFilesRepository extends JpaRepository<ChatFiles, Long> {

}
