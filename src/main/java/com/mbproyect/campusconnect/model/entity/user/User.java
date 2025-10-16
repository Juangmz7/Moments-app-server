package com.juangomez.campusconnect.model.entity.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class User {

    private UUID userId;

    private String email;

    private UserProfile userProfile;

    private boolean isActive;

}
