package com.mbproyect.campusconnect.service;

import com.mbproyect.campusconnect.config.exceptions.auth.InvalidTokenException;
import com.mbproyect.campusconnect.config.exceptions.user.UserAlreadyExistsException;
import com.mbproyect.campusconnect.dto.auth.request.UserAuthRequest;
import com.mbproyect.campusconnect.dto.auth.response.UserAuthenticationResponse;
import com.mbproyect.campusconnect.events.contract.user.UserEventsNotifier;
import com.mbproyect.campusconnect.infrastructure.repository.user.UserRepository;
import com.mbproyect.campusconnect.model.entity.user.User;
import com.mbproyect.campusconnect.model.entity.user.UserProfile;
import com.mbproyect.campusconnect.model.enums.TokenType;
import com.mbproyect.campusconnect.service.auth.ExternalAuthService;
import com.mbproyect.campusconnect.service.auth.JwtService;
import com.mbproyect.campusconnect.service.auth.TokenStorageService;
import com.mbproyect.campusconnect.service.user.UserService;
import com.mbproyect.campusconnect.serviceimpl.auth.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private TokenStorageService tokenStorageService;
    @Mock private UserEventsNotifier userEventsNotifier;
    @Mock private JwtService jwtService;
    @Mock private UserService userService;
    @Mock private ExternalAuthService externalAuthService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void login_ShouldSendVerificationCode_WhenUserExists() {
        // Arrange
        String email = "test@example.com";
        UserAuthRequest request = new UserAuthRequest();
        request.setEmail(email);

        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        authService.login(request);

        // Assert
        verify(tokenStorageService).addToken(anyString(), anyString(), eq(Duration.ofMinutes(10)));
        verify(userEventsNotifier).onUserLoggedEvent(eq(email), anyString());
    }

    @Test
    void register_ShouldThrowException_WhenUserAlreadyExists() {
        // Arrange
        String email = "existing@example.com";
        UserAuthRequest request = new UserAuthRequest();
        request.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));

        // Act & Assert
        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining("Email is already registered");
    }

    @Test
    void validateEmailCode_ShouldReturnAuthResponse_WhenTokenIsValid() {
        // Arrange
        String email = "test@example.com";
        String code = "123456";
        UserAuthRequest request = new UserAuthRequest();
        request.setEmail(email);

        String key = TokenType.concatenate(email, TokenType.VERIFICATION_CODE);
        User user = new User();
        UserProfile profile = new UserProfile();
        profile.setUserName("testuser");
        user.setUserProfile(profile);

        when(tokenStorageService.isTokenValid(key)).thenReturn(true);
        when(tokenStorageService.getToken(key)).thenReturn(code);
        when(userService.findUserByEmail(email)).thenReturn(Optional.of(user));
        when(jwtService.generateToken(email)).thenReturn("jwt-token");

        // Act
        UserAuthenticationResponse response = authService.validateEmailCode(code, request);

        // Assert
        assertThat(response.getEmail()).isEqualTo(email);
        assertThat(response.getAccessToken()).isEqualTo("jwt-token");
        verify(tokenStorageService).removeToken(key);
    }

    @Test
    void validateEmailCode_ShouldThrowException_WhenTokenIsInvalid() {
        // Arrange
        String email = "test@example.com";
        UserAuthRequest request = new UserAuthRequest();
        request.setEmail(email);
        String key = TokenType.concatenate(email, TokenType.VERIFICATION_CODE);

        when(tokenStorageService.isTokenValid(key)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> authService.validateEmailCode("wrong", request))
                .isInstanceOf(InvalidTokenException.class);
    }
}