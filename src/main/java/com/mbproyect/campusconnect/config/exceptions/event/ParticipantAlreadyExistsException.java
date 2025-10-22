package com.mbproyect.campusconnect.config.exceptions.event;

public class ParticipantAlreadyExistsException extends RuntimeException{
    public ParticipantAlreadyExistsException(String message) {
        super(message);
    }
}
