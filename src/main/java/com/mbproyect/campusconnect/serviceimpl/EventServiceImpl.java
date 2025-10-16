package com.juangomez.campusconnect.serviceimpl;

import com.juangomez.campusconnect.config.exceptions.event.EventNotFoundException;
import com.juangomez.campusconnect.dto.event.EventRequest;
import com.juangomez.campusconnect.dto.event.EventResponse;
import com.juangomez.campusconnect.infrastructure.mappers.event.EventBioMapper;
import com.juangomez.campusconnect.infrastructure.mappers.event.EventMapper;
import com.juangomez.campusconnect.model.entity.event.Event;
import com.juangomez.campusconnect.model.entity.event.EventBio;
import com.juangomez.campusconnect.model.enums.InterestTag;
import com.juangomez.campusconnect.infrastructure.repository.EventRepository;
import com.juangomez.campusconnect.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j  // For showing logs
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    private Set<EventResponse> eventSetToResponse (Set<Event> events) {
        if (events.isEmpty()) return Set.of();

        return events.stream()
                .map(EventMapper::toResponse)
                .collect(Collectors.toSet());
    }

    @Override
    public EventResponse getEventById(UUID id) {
        Event event = eventRepository.getEventByEventId(id);

        if ( event == null ) {
            log.error("Event with id {} not found", id);
            throw new EventNotFoundException("Event not found");
        }

        log.info("Returning event with id {}", id);
        return EventMapper.toResponse(event);  // Parse event to event
    }

    @Override
    public Set<EventResponse> getEventsByTag(Set<InterestTag> tags) {
        Set<Event> events = eventRepository.findByEventTag(tags);

        log.info("Returning events with interest tags:  {}", tags);
        return eventSetToResponse(events);
    }

    @Override
    public List<EventResponse> getEventsByDateAscending(Date eventDate) {
        Set<Event> events = eventRepository.findByDate(eventDate);

        if (events.isEmpty()) return List.of();

        log.info("Returning events with date:  {}", eventDate);

        return events.stream()
                .map(EventMapper::toResponse)
                .toList();
    }

    @Override
    public Set<EventResponse> getEventsByLocation(String city) {
        Set<Event> events = eventRepository.findByLocation_City(city);

        log.info("Returning events in {}", city);

        return eventSetToResponse(events);
    }

    @Override
    public EventResponse createEvent(EventRequest eventRequest) {
        Event event = EventMapper.fromRequest(eventRequest);

        eventRepository.save(event);

        return this.getEventById(event.getEventId());
    }

    @Override
    public EventResponse updateEvent(EventRequest eventRequest, UUID eventId) {
        //  Find existing event or throw exception if not found
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + eventId));

        boolean hasChanged = false; // Flag to track if any field was modified

        // Compare and update each field if changed
        if (!Objects.equals(event.getName(), eventRequest.getName())) {
            event.setName(eventRequest.getName());
            hasChanged = true;
        }

        EventBio eventBio = EventBioMapper.fromRequest(eventRequest.getEventBio());

        if (!Objects.equals(event.getEventBio(), eventBio)) {
            event.setEventBio(eventBio);
            hasChanged = true;
        }

        if (!Objects.equals(event.getOrganiser(), eventRequest.getOrganiser())) {
            event.setOrganiser(eventRequest.getOrganiser());
            hasChanged = true;
        }

        if (!Objects.equals(event.getLocation(), eventRequest.getLocation())) {
            event.setLocation(eventRequest.getLocation());
            hasChanged = true;
        }

        if (!Objects.equals(event.getDate(), eventRequest.getDate())) {
            event.setDate(eventRequest.getDate());
            hasChanged = true;
        }

        // Update event tags only if they exist and changed
        if (eventRequest.getEventBio() != null &&
                !Objects.equals(event.getEventTag(), eventRequest.getEventTag())) {
            event.setEventTag(eventRequest.getEventTag());
            hasChanged = true;
        }

        //  Persist changes only if something was updated
        if (hasChanged) {
            log.info("Updating event {} due to modified fields", eventId);
            event = eventRepository.save(event);
        } else {
            log.info("No changes detected for event {}", eventId);
        }

        // Convert and return the updated event as a response DTO
        return EventMapper.toResponse(event);
    }

    @Override
    public void deleteEvent(UUID eventId) {
        getEventById(eventId);
        eventRepository.deleteById(eventId);
        log.info("Event with id {} deleted", eventId);
    }

}
