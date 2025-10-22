package com.mbproyect.campusconnect.infrastructure.repository.chat;

import com.mbproyect.campusconnect.model.entity.chat.EventChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChatRepository extends JpaRepository<EventChat, UUID> {
    EventChat findEventChatById(UUID id);
}
