package com.mbproyect.campusconnect.repository;

import com.mbproyect.campusconnect.config.TestContainersConfig;
import com.mbproyect.campusconnect.infrastructure.repository.user.UserProfileRepository;
import com.mbproyect.campusconnect.infrastructure.repository.user.UserRepository;
import com.mbproyect.campusconnect.model.entity.user.User;
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
class UserRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Test
    void findByEmail_ShouldReturnUser_WhenUserExists() {
        // Arrange
        String email = "integration@test.com";

        UserProfile profile = new UserProfile();
        profile.setUserName("integrationUser");
        profile = entityManager.persistAndFlush(profile);

        User user = new User();
        user.setEmail(email);
        user.setUserProfile(profile);
        entityManager.persistAndFlush(user);

        // Act
        Optional<User> found = userRepository.findByEmail(email);

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo(email);
        assertThat(found.get().getUserProfile().getUserName()).isEqualTo("integrationUser");
    }

    @Test
    void findByUserName_ShouldReturnProfile() {
        // Arrange
        String username = "uniqueUser";
        UserProfile profile = new UserProfile();
        profile.setUserName(username);
        entityManager.persistAndFlush(profile);

        // Act
        Optional<UserProfile> found = userProfileRepository.findByUserName(username);

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getUserName()).isEqualTo(username);
    }
}