package com.mbproyect.campusconnect.dto.eventchat.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageRequest {

    private UUID chatId;

    @NotBlank(message = "Message content cannot be empty")
    private String content;
}
