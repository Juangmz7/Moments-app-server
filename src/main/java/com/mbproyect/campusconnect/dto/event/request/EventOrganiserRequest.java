package com.mbproyect.campusconnect.dto.event.request;

import com.mbproyect.campusconnect.dto.user.request.UserProfileRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventOrganiserRequest {

    private UserProfileRequest userProfile;

    private Set<UUID> eventsIds;
}
