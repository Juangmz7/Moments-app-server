package com.mbproyect.campusconnect.service.user;

import org.springframework.stereotype.Service;

@Service
public interface UserService {

    // Extracts current's user email from security context
    String getCurrentUser();

    void validateCurrentUser(String email);

}
