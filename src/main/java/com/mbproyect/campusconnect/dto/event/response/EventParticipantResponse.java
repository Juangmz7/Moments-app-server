package com.mbproyect.campusconnect.dto.event;

import com.mbproyect.campusconnect.dto.user.UserProfileResponse;
import com.mbproyect.campusconnect.model.entity.event.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventParticipantResponse {

    private String username;

    private UserProfileResponse userProfileResponse;

    private Event event;

}
