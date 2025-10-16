package com.juangomez.campusconnect.infrastructure.mappers.event;

import com.juangomez.campusconnect.dto.event.EventBioRequest;
import com.juangomez.campusconnect.model.entity.event.EventBio;
import com.juangomez.campusconnect.model.enums.InterestTag;

import java.util.HashSet;
import java.util.Set;

public class EventBioMapper {

    /**
     * Converts an EventBioRequest DTO (from client) into an EventBio entity.
     */
    public static EventBio fromRequest(EventBioRequest request) {
        if (request == null) {
            return null;
        }

        Set<InterestTag> tags = request.getInterestTags() != null
                ? new HashSet<>(request.getInterestTags())
                : Set.of();

        return new EventBio(
                request.getDescription(),
                request.getImage(),
                tags
        );
    }

    /**
     * Converts an EventBio entity into an EventBioRequest DTO (useful for returning data to clients).
     */
    public static EventBioRequest toRequest(EventBio eventBio) {
        if (eventBio == null) {
            return null;
        }

        EventBioRequest dto = new EventBioRequest();
        dto.setDescription(eventBio.getDescription());
        dto.setImage(eventBio.getImage());
        dto.setInterestTags(eventBio.getInterestTags() != null
                ? new HashSet<>(eventBio.getInterestTags())
                : Set.of());

        return dto;
    }
}
