package com.mbproyect.campusconnect.serviceimpl.auth;

import com.mbproyect.campusconnect.config.exceptions.user.InvalidTokenException;
import com.mbproyect.campusconnect.config.exceptions.user.UserAlreadyExistsException;
import com.mbproyect.campusconnect.config.exceptions.user.UserNotFoundException;
import com.mbproyect.campusconnect.dto.auth.request.RefreshTokenRequest;
import com.mbproyect.campusconnect.dto.auth.request.UserAuthRequest;
import com.mbproyect.campusconnect.dto.auth.response.UserAuthenticationResponse;
import com.mbproyect.campusconnect.events.contract.user.UserEventsNotifier;
import com.mbproyect.campusconnect.infrastructure.repository.user.UserRepository;
import com.mbproyect.campusconnect.model.entity.user.User;
import com.mbproyect.campusconnect.model.entity.user.UserProfile;
import com.mbproyect.campusconnect.model.enums.TokenType;
import com.mbproyect.campusconnect.service.auth.AuthService;
import com.mbproyect.campusconnect.service.auth.JwtService;
import com.mbproyect.campusconnect.service.auth.TokenStorageService;
import com.mbproyect.campusconnect.shared.util.EncryptionUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final TokenStorageService tokenStorageService;

    private final UserEventsNotifier userEventsNotifier;

    private final JwtService jwtService;

    @Value("${app.activate.link}")
    private String baseUrl;

    public AuthServiceImpl (
            UserRepository userRepository,
            TokenStorageService tokenStorageService,
            UserEventsNotifier userEventsNotifier,
            JwtService jwtService) {
        this.userRepository = userRepository;
        this.tokenStorageService = tokenStorageService;
        this.userEventsNotifier = userEventsNotifier;
        this.jwtService = jwtService;
    }

    private void validateToken (TokenType tokenType, String email, String token) {
        String key = TokenType.concatenate(
                email, tokenType
        );
        if (!tokenStorageService.isTokenValid(key)) {
            throw new InvalidTokenException("The token provided is not valid");
        }

        String savedToken = tokenStorageService.getToken(key);

        if (!token.equals(savedToken)) {
            throw new InvalidTokenException("The token provided is not valid");
        }
    }

    @Override
    public String login(UserAuthRequest userAuthRequest) {
        // Check if user exists
        User user = userRepository
                .findByEmail(userAuthRequest.getEmail())
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "Email not found, please register an account first"
                        )
                );

        // Generate verification code
        String verificationCode = EncryptionUtil.generateNumericCode(8);

        String key = TokenType.concatenate(
                user.getEmail(), TokenType.VERIFICATION_CODE
        );
        tokenStorageService.addToken(
                key, verificationCode, TokenType.VERIFICATION_CODE.getTtlMinutes()
        );

        // Send email with the token
        userEventsNotifier.onUserLoggedEvent(user.getEmail(), verificationCode);
        return "If email is registered, you will receive a verification code";
    }

    @Override
    public void register(UserAuthRequest userAuthRequest) {
        Optional<User> user = userRepository.findByEmail(userAuthRequest.getEmail());

        // Check if user is registered already
        if (user.isPresent()) {
            throw new UserAlreadyExistsException("Email is already registered");
        }

        // Generate account activation token
        UUID activatingToken = EncryptionUtil.generateToken();
        String url = baseUrl + "token=" + activatingToken;
        String key = TokenType
                .concatenate(userAuthRequest.getEmail(), TokenType.ACTIVATION_TOKEN);

        tokenStorageService.addToken(
                key, activatingToken.toString(), TokenType.ACTIVATION_TOKEN.getTtlMinutes()
        );

        // Send email with activation token
        userEventsNotifier.onUserRegisteredEvent(userAuthRequest.getEmail(), url);
    }

    @Override
    public UserAuthenticationResponse validateEmailCode(String verificationToken, UserAuthRequest request) {
        validateToken(
                TokenType.VERIFICATION_CODE, request.getEmail(), verificationToken
        );

        // Return successfully login
        String jwt = jwtService.generateToken(request.getEmail());
        UUID refreshToken = EncryptionUtil.generateToken();

        String key = TokenType.concatenate(request.getEmail(), TokenType.REFRESH_TOKEN);
        tokenStorageService.addToken(
                key, refreshToken.toString(), TokenType.REFRESH_TOKEN.getTtlMinutes()
        );

        return new UserAuthenticationResponse(
                request.getEmail(), jwt, refreshToken
        );
    }

    @Override
    public void activateAccount(String activatingToken, UserAuthRequest request) {
        // Check if the token is valid
        validateToken(TokenType.ACTIVATION_TOKEN, request.getEmail(), activatingToken);

        // Token is valid so we register the user
        String initialUsername = request.getEmail().substring(0, 6);
        var userProfile = new UserProfile();
        userProfile.setUserName(initialUsername);

        var user = new User();
        user.setEmail(request.getEmail());
        user.setUserProfile(userProfile);
        user.setActive(true);
        userRepository.save(user);
    }

    @Override
    public UserAuthenticationResponse refreshToken(RefreshTokenRequest request) {
        validateToken(
                TokenType.REFRESH_TOKEN, request.getEmail(), request.getRefreshToken()
        );

        String jwt = jwtService.generateToken(request.getEmail());
        UUID refreshToken = EncryptionUtil.generateToken();

        return new UserAuthenticationResponse(
                request.getEmail(), jwt, refreshToken
        );
    }

    @Override
    public void logout(HttpServletRequest request) {
        String token = jwtService.extractAuthToken(request);
        // The token is not valid
        if (token == null) {
            return;
        }
        // Remove the token to the validation list
        tokenStorageService.removeToken(token);
    }
}
