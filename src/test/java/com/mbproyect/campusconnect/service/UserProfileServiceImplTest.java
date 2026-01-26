package com.mbproyect.campusconnect.service;

import com.mbproyect.campusconnect.dto.user.request.UserProfileRequest;
import com.mbproyect.campusconnect.dto.user.response.UserProfileResponse;
import com.mbproyect.campusconnect.infrastructure.repository.user.UserProfileRepository;
import com.mbproyect.campusconnect.model.entity.user.UserProfile;
import com.mbproyect.campusconnect.serviceimpl.user.UserProfileServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceImplTest {

    @Mock private UserProfileRepository userProfileRepository;

    @InjectMocks
    private UserProfileServiceImpl userProfileService;

    @Test
    void update_ShouldOnlyUpdateChangedFields() {
        // Arrange
        UUID id = UUID.randomUUID();
        UserProfile existing = new UserProfile();
        existing.setId(id);
        existing.setUserName("OldName");
        existing.setAge(20);

        UserProfileRequest request = new UserProfileRequest();
        request.setUserName("NewName");
        request.setAge(20); // Same age, should not trigger change logic for this field

        when(userProfileRepository.findById(id)).thenReturn(Optional.of(existing));
        when(userProfileRepository.save(any(UserProfile.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        UserProfileResponse response = userProfileService.update(id, request, null);

        // Assert
        assertThat(response.getUserName()).isEqualTo("NewName");
        verify(userProfileRepository).save(existing);
    }
}