package com.mbproyect.campusconnect.serviceimpl.user;

import com.mbproyect.campusconnect.service.user.UserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Access denied: User not authenticated");
        }
        // Principal object must be an instance of UserPrincipal
        if (!(authentication.getPrincipal() instanceof String email)) {
            throw new IllegalStateException("Config error, principal is not UserPrincipal");
        }
        return email;
    }

    @Override
    public void validateCurrentUser(String email) {
        if (!this.getCurrentUser().equals(email)) {
            throw new IllegalStateException(
                    "The current user is not authorized to perform this operation"
            );
        }
    }
}
