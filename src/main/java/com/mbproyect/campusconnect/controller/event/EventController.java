package com.juangomez.campusconnect.controller.event;

import com.juangomez.campusconnect.dto.event.EventRequest;
import com.juangomez.campusconnect.dto.event.EventResponse;
import com.juangomez.campusconnect.model.entity.event.Event;
import com.juangomez.campusconnect.model.entity.event.EventLocation;
import com.juangomez.campusconnect.model.entity.event.EventParticipant;
import com.juangomez.campusconnect.model.enums.InterestTag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/event")
public class EventController {

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEventById (@PathVariable int id) {
        return ResponseEntity.ok(new EventResponse());
    }

    @GetMapping("/participants/{id}")
    public ResponseEntity<List<EventParticipant>> getEventParticipants (@PathVariable int id) {
        return ResponseEntity.ok(List.of(new EventParticipant()));
    }

    @GetMapping
    public ResponseEntity<List<EventResponse>> getEventsByTag (@RequestParam InterestTag tag) {
        return ResponseEntity.ok(List.of(new EventResponse()));
    }

    @GetMapping
    public ResponseEntity<List<EventResponse>> getEventsByDateAscending (@RequestParam Date eventDate) {
        return ResponseEntity.ok(List.of(new EventResponse()));
    }

    @GetMapping
    public ResponseEntity<List<EventResponse>> getEventsByLocation (@RequestParam EventLocation location) {
        return ResponseEntity.ok(List.of(new EventResponse()));
    }

    @PostMapping
    public ResponseEntity<EventResponse> createEvent (@RequestBody EventRequest eventRequest) {
        return ResponseEntity.ok(new Event());
    }

    @PutMapping
    public ResponseEntity<EventResponse> updateEvent (@RequestBody EventRequest eventRequest) {
        return ResponseEntity.ok(new EventResponse());
    }


}
