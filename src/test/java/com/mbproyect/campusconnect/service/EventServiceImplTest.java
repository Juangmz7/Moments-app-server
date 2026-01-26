package com.mbproyect.campusconnect.service;

import com.mbproyect.campusconnect.config.exceptions.event.InvalidDateException;
import com.mbproyect.campusconnect.dto.event.request.EventBioRequest;
import com.mbproyect.campusconnect.dto.event.request.EventRequest;
import com.mbproyect.campusconnect.dto.event.response.EventResponse;
import com.mbproyect.campusconnect.events.contract.event.EventEventsNotifier;
import com.mbproyect.campusconnect.infrastructure.mappers.event.EventBioMapper;
import com.mbproyect.campusconnect.infrastructure.repository.event.EventRepository;
import com.mbproyect.campusconnect.model.entity.chat.EventChat;
import com.mbproyect.campusconnect.model.entity.event.Event;
import com.mbproyect.campusconnect.model.entity.event.EventBio;
import com.mbproyect.campusconnect.model.entity.event.EventLocation;
import com.mbproyect.campusconnect.model.entity.event.EventOrganiser;
import com.mbproyect.campusconnect.model.entity.user.UserLocation;
import com.mbproyect.campusconnect.service.chat.EventChatService;
import com.mbproyect.campusconnect.service.event.EventOrganiserService;
import com.mbproyect.campusconnect.service.user.UserService;
import com.mbproyect.campusconnect.serviceimpl.event.EventServiceImpl;
import com.mbproyect.campusconnect.shared.validation.event.EventValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Mock private EventRepository eventRepository;
    @Mock private EventValidator eventValidator;
    @Mock private EventChatService eventChatService;
    @Mock private EventEventsNotifier eventsNotifier;
    @Mock private UserService userService;
    @Mock private EventOrganiserService eventOrganiserService;

    @InjectMocks
    private EventServiceImpl eventService;

    @Test
    void createEvent_ShouldSucceed_WhenDatesAreValid() {
        // Arrange
        String email = "organiser@test.com";
        EventRequest request = new EventRequest();
        request.setName("Hackathon");
        request.setStartDate(LocalDateTime.now().plusDays(1));
        request.setEndDate(LocalDateTime.now().plusDays(1).plusHours(2));
        // Mock Bio and Location to avoid NullPointer in Mapper if they are accessed
        EventBio bio = new EventBio();
        bio.setDescription("Code cool stuff");
        request.setEventBio(request.getEventBio());
        request.setLocation(new EventLocation("Madrid", "Spain"));

        MultipartFile imageMock = mock(MultipartFile.class); // Mock empty file
        when(imageMock.isEmpty()).thenReturn(true);

        when(userService.getCurrentUser()).thenReturn(email);
        when(eventOrganiserService.getEventOrganiserByEmail(eq(email), any(Event.class)))
                .thenReturn(new EventOrganiser());
        when(eventChatService.createChat(any(Event.class))).thenReturn(new EventChat());

        // Mock getEventById behavior since createEvent calls it at the end
        Event savedEvent = new Event();
        savedEvent.setEventId(UUID.randomUUID());
        savedEvent.setName("Hackathon");
        savedEvent.setStartDate(request.getStartDate());
        savedEvent.setEventBio(bio); // Ensure bio is present

        when(eventRepository.save(any(Event.class))).thenAnswer(invocation -> {
            Event e = invocation.getArgument(0);
            e.setEventId(UUID.randomUUID()); // simulate DB ID generation
            return e;
        });

        // Mock the validation inside getEventById
        when(eventValidator.validateEventExists(any())).thenReturn(savedEvent);

        // Act
        EventResponse response = eventService.createEvent(request, imageMock);

        // Assert
        assertThat(response.getName()).isEqualTo("Hackathon");
        verify(eventRepository, times(2)).save(any(Event.class)); // 1 init, 1 with chat
    }

    @Test
    void createEvent_ShouldThrowException_WhenEndDateBeforeStartDate() {
        // Arrange
        EventRequest request = new EventRequest();
        request.setStartDate(LocalDateTime.now().plusDays(2));
        request.setEndDate(LocalDateTime.now().plusDays(1)); // Invalid

        // Act & Assert
        assertThatThrownBy(() -> eventService.createEvent(request, null))
                .isInstanceOf(InvalidDateException.class)
                .hasMessageContaining("End date must be after start date");
    }

    @Test
    void updateEvent_ShouldUpdateFields_WhenUserIsOrganiser() {
        // Arrange
        UUID eventId = UUID.randomUUID();
        String organiserEmail = "org@test.com";

        // 1. Setup the Request
        EventRequest request = new EventRequest();
        request.setName("New Name");
        request.setStartDate(LocalDateTime.now().plusDays(5));
        request.setEndDate(LocalDateTime.now().plusDays(5).plusHours(3));
        request.setLocation(new EventLocation("Madrid", "Venue")); // Ensure location is not null if checked

        EventBioRequest bioRequest = new EventBioRequest();
        bioRequest.setDescription("New Description");
        request.setEventBio(bioRequest);

        // Setup existing event in DB (Mock)
        Event existingEvent = new Event();
        existingEvent.setEventId(eventId);
        existingEvent.setName("Old Name");
        existingEvent.setStartDate(LocalDateTime.now().plusDays(5));
        existingEvent.setEndDate(LocalDateTime.now().plusDays(5).plusHours(3));
        existingEvent.setEventBio(EventBioMapper.fromRequest(request.getEventBio()));

        EventOrganiser organiser = new EventOrganiser();
        organiser.setEmail(organiserEmail);
        existingEvent.setOrganiser(organiser);

        EventBio existingBio = new EventBio();
        existingBio.setDescription("Old Description");
        existingEvent.setEventBio(existingBio);
        existingEvent.setLocation(new EventLocation("Madrid", "Venue"));

        // Mocks
        when(eventValidator.validateEventExists(eventId)).thenReturn(existingEvent);
        when(eventRepository.save(any(Event.class))).thenReturn(existingEvent);

        // Act
        eventService.updateEvent(request, eventId, null);

        // Assert
        verify(userService).validateCurrentUser(organiserEmail);
        verify(eventsNotifier).onEventChanged(any(), anyList(), anyList());
        assertThat(existingEvent.getName()).isEqualTo("New Name");
        assertThat(existingEvent.getEventBio().getDescription()).isEqualTo("New Description");
    }
}