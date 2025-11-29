package com.mbproyect.campusconnect.infrastructure.repository.event;

import com.mbproyect.campusconnect.model.entity.event.Event;
import com.mbproyect.campusconnect.model.entity.event.EventParticipant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface EventParticipantRepository extends JpaRepository<EventParticipant, UUID> {
    Optional<EventParticipant> findEventParticipantByEvent_EventIdAndUserProfile_Id(
            UUID eventEventId, UUID userProfileId
    );

    Page<EventParticipant> findEventParticipantsByEvent(
            Event event,
            Pageable pageable
    );

    Set<EventParticipant> getEventParticipantsByEvent_Chat_Id(UUID eventChatId);
}
