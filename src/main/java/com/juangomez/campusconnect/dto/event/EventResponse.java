package com.juangomez.campusconnect.dto.event;

import com.juangomez.campusconnect.entity.event.EventOrganiser;
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

    private String eventBio;

    private EventOrganiser organiser;

    private String location;

    private Date date;

}
