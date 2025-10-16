package com.juangomez.campusconnect.config.exceptions.event;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(String message) {
        super(message);
    }
}