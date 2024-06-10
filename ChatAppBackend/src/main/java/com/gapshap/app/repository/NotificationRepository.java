package com.gapshap.app.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.gapshap.app.model.chat.Notifications;

public interface NotificationRepository extends JpaRepository<Notifications, Long>{

}
