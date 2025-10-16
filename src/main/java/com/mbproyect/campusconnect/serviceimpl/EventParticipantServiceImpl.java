package com.juangomez.campusconnect.serviceimpl;

import com.juangomez.campusconnect.config.exceptions.event.EventNotFoundException;
import com.juangomez.campusconnect.dto.event.EventParticipantResponse;
import com.juangomez.campusconnect.infrastructure.mappers.event.EventParticipantMapper;
import com.juangomez.campusconnect.infrastructure.repository.EventRepository;
import com.juangomez.campusconnect.model.entity.event.Event;
import com.juangomez.campusconnect.model.entity.event.EventParticipant;
import com.juangomez.campusconnect.service.EventParticipantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EventParticipantServiceImpl implements EventParticipantService {

    private final EventRepository eventRepository;

    public EventParticipantServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Set<EventParticipantResponse> getParticipantsByEvent(UUID eventId) {
        // If event do not exist, it throws a not found exception
        Event event = eventRepository.getEventByEventId(eventId);

        if ( event == null ) {
            log.error("Event with id {} not found", eventId);
            throw new EventNotFoundException("Event not found");
        }

        Set<EventParticipant> participants = event.getParticipants();

        // Returns an empty set
        if (participants.isEmpty()) return Set.of();

        return participants.stream()
                .map(EventParticipantMapper::toResponse) // Call method reference
                .collect(Collectors.toSet());           // Transforms stream to a set
    }

    @Override
    public EventParticipantResponse addParticipant(UUID eventId, UUID userId) {
        return null; // TODO
    }

    @Override
    public void removeParticipant(UUID eventId, UUID userId) {
        // TODO
    }
}
