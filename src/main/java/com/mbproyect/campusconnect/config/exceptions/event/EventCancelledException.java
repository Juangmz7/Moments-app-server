package com.mbproyect.campusconnect.config.exceptions.event;

public class EventCancelledException extends RuntimeException{
    public EventCancelledException (String message) {
        super(message);
    }
}
