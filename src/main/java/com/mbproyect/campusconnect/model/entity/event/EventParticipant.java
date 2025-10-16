package com.juangomez.campusconnect.model.entity.event;

import com.juangomez.campusconnect.model.entity.user.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class EventParticipant {

    private UUID id;

    private UserProfile userProfile;

}
