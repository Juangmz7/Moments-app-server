package com.mbproyect.campusconnect.service.user;

import com.mbproyect.campusconnect.model.entity.user.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {

    // Extracts current's user email from security context
    String getCurrentUser();

    void validateCurrentUser(@NotBlank String email);

    User createUser(@NotBlank String email);

    Optional<User> findUserByEmail (String email);

}
