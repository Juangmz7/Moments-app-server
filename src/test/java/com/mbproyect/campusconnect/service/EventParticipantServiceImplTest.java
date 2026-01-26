package com.mbproyect.campusconnect.service;

import com.mbproyect.campusconnect.config.exceptions.event.ParticipantAlreadyExistsException;
import com.mbproyect.campusconnect.events.contract.event.EventEventsNotifier;
import com.mbproyect.campusconnect.infrastructure.repository.event.EventParticipantRepository;
import com.mbproyect.campusconnect.infrastructure.repository.user.UserRepository;
import com.mbproyect.campusconnect.model.entity.event.Event;
import com.mbproyect.campusconnect.model.entity.event.EventParticipant;
import com.mbproyect.campusconnect.model.entity.user.User;
import com.mbproyect.campusconnect.model.entity.user.UserProfile;
import com.mbproyect.campusconnect.service.user.UserService;
import com.mbproyect.campusconnect.serviceimpl.event.EventParticipantServiceImpl;
import com.mbproyect.campusconnect.shared.validation.event.EventValidator;
import com.mbproyect.campusconnect.shared.validation.user.UserValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventParticipantServiceImplTest {

    @Mock private EventValidator eventValidator;
    @Mock private EventParticipantRepository eventParticipantRepository;
    @Mock private UserValidator userValidator;
    @Mock private EventEventsNotifier eventsNotifier;
    @Mock private UserService userService;
    @Mock private UserRepository userRepository;

    @InjectMocks
    private EventParticipantServiceImpl participantService;

    @Test
    void subscribeToEvent_ShouldSucceed_WhenNotAlreadySubscribed() {
        // Arrange
        UUID eventId = UUID.randomUUID();
        String email = "user@test.com";
        UUID profileId = UUID.randomUUID();

        User user = new User();
        user.setEmail(email);
        UserProfile profile = new UserProfile();
        profile.setId(profileId);
        user.setUserProfile(profile);

        when(userService.getCurrentUser()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        // Return Empty to simulate no existing subscription
        when(eventParticipantRepository.findEventParticipantByEvent_EventIdAndUserProfile_Id(eventId, profileId))
                .thenReturn(Optional.empty());
        when(eventValidator.validateEventExists(eventId)).thenReturn(new Event());

        // Act
        participantService.subscribeToEvent(eventId);

        // Assert
        verify(eventParticipantRepository).save(any(EventParticipant.class));
        verify(eventsNotifier).onParticipantSubscribed(any(), any());
    }

    @Test
    void subscribeToEvent_ShouldThrowException_WhenAlreadySubscribed() {
        // Arrange
        UUID eventId = UUID.randomUUID();
        String email = "user@test.com";
        UUID profileId = UUID.randomUUID();

        User user = new User();
        user.setEmail(email);
        UserProfile profile = new UserProfile();
        profile.setId(profileId);
        user.setUserProfile(profile);

        when(userService.getCurrentUser()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        // Return Present to simulate existing subscription
        when(eventParticipantRepository.findEventParticipantByEvent_EventIdAndUserProfile_Id(eventId, profileId))
                .thenReturn(Optional.of(new EventParticipant()));

        // Act & Assert
        assertThatThrownBy(() -> participantService.subscribeToEvent(eventId))
                .isInstanceOf(ParticipantAlreadyExistsException.class);

        verify(eventParticipantRepository, never()).save(any());
    }
}