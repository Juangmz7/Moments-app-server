package com.mbproyect.campusconnect.serviceimpl.user;

import com.mbproyect.campusconnect.infrastructure.repository.user.UserRepository;
import com.mbproyect.campusconnect.model.entity.user.User;
import com.mbproyect.campusconnect.model.entity.user.UserProfile;
import com.mbproyect.campusconnect.service.user.UserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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

    @Override
    public User createUser(String email) {
        String initialUsername = email.substring(0, 6);
        var userProfile = new UserProfile();
        userProfile.setUserName(initialUsername);

        var user = new User();
        user.setEmail(email);
        user.setUserProfile(userProfile);
        user.setActive(true);
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
