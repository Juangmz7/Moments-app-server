package com.juangomez.campusconnect.model.entity.event;

import com.juangomez.campusconnect.model.enums.EventTag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    private UUID eventId;

    private String name;

    private EventBio eventBio;

    private EventOrganiser organiser;

    private EventLocation location;

    private List<EventParticipant> participants;

    private Date date;

    private List<EventTag> eventTag;

}

