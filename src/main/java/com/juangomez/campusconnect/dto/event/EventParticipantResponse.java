package com.juangomez.campusconnect.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventParticipantResponse {

    private UUID eventParticipantId;

    private String username;

}
