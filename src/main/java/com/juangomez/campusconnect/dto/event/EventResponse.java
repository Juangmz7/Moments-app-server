package com.juangomez.campusconnect.dto.event;

import com.juangomez.campusconnect.model.entity.event.EventBio;
import com.juangomez.campusconnect.model.entity.event.EventLocation;
import com.juangomez.campusconnect.model.entity.event.EventOrganiser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventResponse {

    private UUID eventId;

    private String name;

    private EventBio eventBio;

    private EventOrganiser organiser;

    private EventLocation location;

    private Date date;

}
