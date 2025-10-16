package com.juangomez.campusconnect.service;

import com.juangomez.campusconnect.dto.event.EventParticipantResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Set;
import java.util.UUID;

@Service
@Validated
public interface EventParticipantService {

    Set<EventParticipantResponse> getParticipantsByEvent(@NotNull UUID eventId);

    EventParticipantResponse addParticipant(@NotNull UUID eventId, @NotNull UUID userId);

    void removeParticipant(@NotNull UUID eventId, @NotNull UUID userId);
}
