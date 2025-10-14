package com.juangomez.campusconnect.controller.event;

import com.juangomez.campusconnect.dto.event.EventRequest;
import com.juangomez.campusconnect.dto.event.EventResponse;
import com.juangomez.campusconnect.entity.event.Event;
import com.juangomez.campusconnect.entity.event.EventParticipant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/event")
public class EventController {

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable int id) {
        return ResponseEntity.ok(new EventResponse());
    }

    @GetMapping("/participants/{id}")
    public ResponseEntity<EventParticipant> getEventParticipants(@PathVariable int id) {
        return ResponseEntity.ok(new EventParticipant());
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody EventRequest eventRequest) {
        return ResponseEntity.ok(new Event());
    }

    @PutMapping
    public ResponseEntity<EventResponse> updateEvent(@RequestBody EventRequest eventRequest) {
        return ResponseEntity.ok(new EventResponse());
    }


}
