package com.juangomez.campusconnect.infrastructure.repository;

import com.juangomez.campusconnect.model.entity.event.Event;
import com.juangomez.campusconnect.model.entity.event.EventParticipant;
import com.juangomez.campusconnect.model.enums.InterestTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
    Event getEventByEventId(UUID eventId);

    Set<Event> findByEventTag(Set<InterestTag> eventTag);

    Set<Event> findByDate(Date date);

    Set<Event> findByLocation_City(String locationCity);
}
