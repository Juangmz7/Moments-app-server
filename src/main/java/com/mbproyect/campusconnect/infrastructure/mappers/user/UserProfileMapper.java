package com.juangomez.campusconnect.infrastructure.mappers.user;

import com.juangomez.campusconnect.dto.user.UserProfileResponse;
import com.juangomez.campusconnect.model.entity.user.UserProfile;
import com.juangomez.campusconnect.model.entity.user.UserLocation;

import java.util.HashSet;
import java.util.Set;

public class UserProfileMapper {

    /**
     * Converts a UserProfile entity into a UserProfileResponse DTO.
     */
    public static UserProfileResponse toResponse(UserProfile userProfile) {
        if (userProfile == null) {
            return null;
        }

        UserProfileResponse response = new UserProfileResponse();
        response.setUserName(userProfile.getUserName());
        response.setNationality(userProfile.getNationality());
        response.setLanguages(
                userProfile.getLanguages() != null ? new HashSet<>(userProfile.getLanguages()) : Set.of()
        );
        response.setInterests(
                userProfile.getInterests() != null ? new HashSet<>(userProfile.getInterests()) : Set.of()
        );
        response.setUserLocation(copyLocation(userProfile.getUserLocation()));

        return response;
    }

    /**
     * Copies the nested UserLocation object safely.
     */
    private static UserLocation copyLocation(UserLocation source) {
        if (source == null) {
            return null;
        }
        UserLocation location = new UserLocation();
        location.setCity(source.getCity());
        location.setCountry(source.getCountry());
        return location;
    }
}

