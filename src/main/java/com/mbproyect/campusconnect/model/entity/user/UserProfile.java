package com.juangomez.campusconnect.model.entity.user;

import com.juangomez.campusconnect.model.enums.InterestTag;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UserProfile {

    private UUID id;

    private String userName;

    private String nationality;

    private Set<String> languages;

    private Set<InterestTag> interests;

    private UserLocation userLocation;

}
