package com.mbproyect.campusconnect.dto.user;

import com.mbproyect.campusconnect.model.entity.user.UserLocation;
import com.mbproyect.campusconnect.model.enums.InterestTag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {

    private String userName;

    private String nationality;

    private Set<String> languages;

    private int age;

    private Set<InterestTag> interests;

    private UserLocation userLocation;
}
