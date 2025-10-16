package com.juangomez.campusconnect.controller.auth;

import com.juangomez.campusconnect.dto.auth.UserAuthRequest;
import com.juangomez.campusconnect.dto.auth.UserAuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserAuthRequest userAuthRequest) {
        return ResponseEntity.ok("jwt token");
    }

    @PostMapping("/register")
    public ResponseEntity<UserAuthenticationResponse> register(@RequestBody UserAuthRequest userAuthRequest) {
        return ResponseEntity.ok(new UserAuthenticationResponse(UUID.randomUUID(), "example_email"));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        return ResponseEntity.noContent().build();
    }

}
