package com.juangomez.campusconnect.infrastructure.mappers.event;

import com.juangomez.campusconnect.dto.event.EventParticipantResponse;
import com.juangomez.campusconnect.dto.user.UserProfileResponse;
import com.juangomez.campusconnect.infrastructure.mappers.user.UserProfileMapper;
import com.juangomez.campusconnect.model.entity.event.EventParticipant;
import com.juangomez.campusconnect.model.entity.user.UserProfile;

public class EventParticipantMapper {

    /**
     * Converts an EventParticipant entity into an EventParticipantResponse DTO.
     */
    public static EventParticipantResponse toResponse(EventParticipant participant) {
        if (participant == null) {
            return null;
        }

        UserProfile userProfile = participant.getUserProfile();

        // Reuse the existing UserProfileMapper
        UserProfileResponse userProfileResponse = UserProfileMapper.toResponse(userProfile);

        EventParticipantResponse response = new EventParticipantResponse();
        response.setUsername(userProfile != null ? userProfile.getUserName() : null);
        response.setUserProfileResponse(userProfileResponse);

        return response;
    }
}
