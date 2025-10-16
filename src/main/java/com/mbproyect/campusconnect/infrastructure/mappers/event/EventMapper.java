package com.juangomez.campusconnect.infrastructure.mappers.event;

import com.juangomez.campusconnect.dto.event.EventRequest;
import com.juangomez.campusconnect.dto.event.EventResponse;
import com.juangomez.campusconnect.model.entity.event.Event;

import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Collectors;

public class EventMapper {

    /***
     * Converts an EventRequest (coming from the client) into an Event entity.
     */
    public static Event fromRequest(EventRequest request) {
        if (request == null) {
            return null;
        }

        Event event = new Event();

        // Assign a new UUID (if not generated automatically in DB)
        event.setEventId(UUID.randomUUID());

        event.setName(request.getName());
        event.setEventBio(request.getEventBio());
        event.setOrganiser(request.getOrganiser());
        event.setLocation(request.getLocation());
        event.setDate(request.getDate());

        // Tags are not in EventRequest yet (optional)
        event.setEventTag(new HashSet<>());

        return event;
    }

    /***
     * Converts an Event entity into an EventResponse (for returning to the client).
     */
    public static EventResponse toResponse(Event event) {
        if (event == null) {
            return null;
        }

        EventResponse response = new EventResponse();
        response.setEventId(event.getEventId());
        response.setName(event.getName());
        response.setEventBio(event.getEventBio());
        response.setOrganiser(event.getOrganiser());
        response.setLocation(event.getLocation());
        response.setDate(event.getDate());

        if (event.getEventTag() != null) {
            response.setEventTag(event.getEventTag().stream().collect(Collectors.toSet()));
        }

        return response;
    }
}