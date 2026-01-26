package com.mbproyect.campusconnect.repository;

import com.mbproyect.campusconnect.config.TestContainersConfig;
import com.mbproyect.campusconnect.infrastructure.repository.event.EventParticipantRepository;
import com.mbproyect.campusconnect.model.entity.chat.EventChat;
import com.mbproyect.campusconnect.model.entity.event.Event;
import com.mbproyect.campusconnect.model.entity.event.EventParticipant;
import com.mbproyect.campusconnect.model.entity.user.UserProfile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestContainersConfig.class)
class EventParticipantRepositoryIntegrationTest {

    @Autowired private TestEntityManager entityManager;
    @Autowired private EventParticipantRepository participantRepository;

    @Test
    void findByEmailAndChatId_ShouldReturnParticipant() {
        Event event = new Event();
        event.setName("Chat Event");

        EventChat chat = new EventChat();
        chat.setEvent(event);

        event = entityManager.persist(event);
        chat = entityManager.persist(chat);
        event.setChat(chat);
        entityManager.merge(event);

        // Create Participant
        String email = "chatuser@test.com";
        UserProfile profile = new UserProfile();
        profile.setUserName("chatuser");
        entityManager.persist(profile);

        EventParticipant participant = new EventParticipant();
        participant.setEvent(event);
        participant.setUserProfile(profile);
        participant.setEmail(email);
        entityManager.persist(participant);

        entityManager.flush();

        // Act
        Optional<EventParticipant> result = participantRepository
                .findByEmailAndChatId(chat.getId(), email);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo(email);
    }
}