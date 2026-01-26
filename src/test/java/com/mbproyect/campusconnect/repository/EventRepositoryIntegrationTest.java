package com.mbproyect.campusconnect.repository;

import com.mbproyect.campusconnect.config.TestContainersConfig;
import com.mbproyect.campusconnect.infrastructure.repository.event.EventRepository;
import com.mbproyect.campusconnect.model.entity.event.*;
import com.mbproyect.campusconnect.model.entity.user.UserProfile;
import com.mbproyect.campusconnect.model.enums.EventStatus;
import com.mbproyect.campusconnect.model.enums.InterestTag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestContainersConfig.class)
class EventRepositoryIntegrationTest {

    @Autowired private TestEntityManager entityManager;
    @Autowired private EventRepository eventRepository;

    private UserProfile organiserProfile;
    private UserProfile outsiderProfile;

    @BeforeEach
    void setUp() {
        organiserProfile = createProfile("organiser");
        outsiderProfile = createProfile("outsider");
    }

    @Test
    void getUpcomingEvents_ShouldExcludeOwnEventsAndJoinedEvents() {
        createEvent("Organiser Event", organiserProfile, null, LocalDateTime.now().plusDays(2));

        createEvent("Joined Event", outsiderProfile, "organiser", LocalDateTime.now().plusDays(3));

        createEvent("Visible Event", outsiderProfile, null, LocalDateTime.now().plusDays(4));

        // Act
        Page<Event> result = eventRepository.getUpcomingEvents(
                LocalDateTime.now(),
                EventStatus.ACTIVE,
                "organiser@test.com",
                PageRequest.of(0, 10)
        );

        // Assert
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Visible Event");
    }

    @Test
    void findByLocation_City_ShouldFilterByCityAndExcludeUser() {
        // Arrange
        Event eventInMadrid = createEvent("Madrid Event", outsiderProfile, null, LocalDateTime.now().plusDays(1));
        eventInMadrid.setLocation(new EventLocation("Madrid", "Spain"));
        entityManager.persist(eventInMadrid);

        Event eventInLondon = createEvent("London Event", outsiderProfile, null, LocalDateTime.now().plusDays(1));
        eventInLondon.setLocation(new EventLocation("London", "UK"));
        entityManager.persist(eventInLondon);

        entityManager.flush();

        // Act
        Page<Event> result = eventRepository.findByLocation_City(
                "Madrid",
                EventStatus.ACTIVE,
                "me@test.com",
                PageRequest.of(0, 10)
        );

        // Assert
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getLocation().getCity()).isEqualTo("Madrid");
    }

    @Test
    void isUserInEvent_ShouldReturnTrue_ForOrganiserAndParticipant() {
        // Arrange
        Event event = createEvent("Test Event", organiserProfile, "participant", LocalDateTime.now().plusDays(1));

        // Act & Assert
        boolean isOrganiserIn = eventRepository.isUserInEvent("organiser@test.com", event.getEventId(), EventStatus.ACTIVE);
        boolean isParticipantIn = eventRepository.isUserInEvent("participant@test.com", event.getEventId(), EventStatus.ACTIVE);
        boolean isOutsiderIn = eventRepository.isUserInEvent("random@test.com", event.getEventId(), EventStatus.ACTIVE);

        assertThat(isOrganiserIn).isTrue();
        assertThat(isParticipantIn).isTrue();
        assertThat(isOutsiderIn).isFalse();
    }

    // --- Helpers ---

    private UserProfile createProfile(String name) {
        UserProfile p = new UserProfile();
        p.setUserName(name);
        p.setAge(25);
        p.setNationality("Test");
        return entityManager.persistFlushFind(p);
    }

    private Event createEvent(String name, UserProfile organiserProf, String participantName, LocalDateTime date) {
        Event event = new Event();
        event.setName(name);
        event.setStartDate(date);
        event.setEndDate(date.plusHours(2));
        event.setEventStatus(EventStatus.ACTIVE);

        EventBio bio = new EventBio();
        bio.setInterestTags(Set.of(InterestTag.SPORTS));
        entityManager.persist(bio);
        event.setEventBio(bio);

        event.setLocation(new EventLocation("City", "Country"));

        String orgEmail = organiserProf.getUserName() + "@test.com";

        EventOrganiser organiser = null;
        try {
            organiser = entityManager.getEntityManager()
                    .createQuery("SELECT o FROM EventOrganiser o WHERE o.userProfile.id = :pid", EventOrganiser.class)
                    .setParameter("pid", organiserProf.getId())
                    .getSingleResult();
        } catch (Exception e) {
            organiser = new EventOrganiser();
            organiser.setEmail(orgEmail);
            organiser.setUserProfile(organiserProf);
            entityManager.persist(organiser);
        }

        event.setOrganiser(organiser);

        event = entityManager.persist(event);

        if (participantName != null) {
            UserProfile partProfile;
            String partEmail;

            if (participantName.equals("organiser")) {
                partProfile = organiserProfile;
                partEmail = "organiser@test.com";
            } else {
                partProfile = new UserProfile();
                partProfile.setUserName(participantName);
                partProfile.setAge(20);
                partProfile.setNationality("Test");
                entityManager.persist(partProfile);
                partEmail = participantName + "@test.com";
            }

            EventParticipant participant = new EventParticipant();
            participant.setEvent(event);
            participant.setEmail(partEmail);
            participant.setUserProfile(partProfile);

            entityManager.persist(participant);
        }

        entityManager.flush();
        entityManager.refresh(event);

        return event;
    }
}