package com.mbproyect.campusconnect.config.exceptions.event;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
