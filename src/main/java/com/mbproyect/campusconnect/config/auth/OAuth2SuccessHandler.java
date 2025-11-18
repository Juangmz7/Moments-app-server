package com.mbproyect.campusconnect.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mbproyect.campusconnect.dto.auth.response.UserAuthenticationResponse;
import com.mbproyect.campusconnect.model.enums.TokenType;
import com.mbproyect.campusconnect.service.auth.JwtService;
import com.mbproyect.campusconnect.service.auth.TokenStorageService;
import com.mbproyect.campusconnect.service.user.UserService;
import com.mbproyect.campusconnect.shared.util.EncryptionUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserService userService;
    private final TokenStorageService tokenStorageService;

    public OAuth2SuccessHandler(
            JwtService jwtService,
            UserService userService,
            TokenStorageService tokenStorageService
    ) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.tokenStorageService = tokenStorageService;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        var oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        // Search or creates the user in db
        userService.findUserByEmail(email).orElseGet(
                () -> userService.createUser(email)
        );

        // Generate own JWT
        String jwt = jwtService.generateToken(email);
        UUID refreshToken = EncryptionUtil.generateToken();

        String key = TokenType.concatenate(email, TokenType.REFRESH_TOKEN);
        tokenStorageService.addToken(
                key, refreshToken.toString(), TokenType.REFRESH_TOKEN.getTtlMinutes()
        );

        UserAuthenticationResponse authResponse = new UserAuthenticationResponse(
                email, jwt, refreshToken
        );

        // Returns jsons
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        new ObjectMapper().writeValue(response.getWriter(), authResponse);
    }
}

