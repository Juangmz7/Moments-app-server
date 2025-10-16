package com.mbproyect.campusconnect.dto.event.request;

import com.mbproyect.campusconnect.dto.user.request.UserProfileRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventParticipantRequest {

    private UserProfileRequest userProfile;

    private UUID eventId;
}

