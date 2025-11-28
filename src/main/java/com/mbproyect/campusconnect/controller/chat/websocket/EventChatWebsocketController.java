package com.mbproyect.campusconnect.controller.chat.websocket;

import com.mbproyect.campusconnect.dto.chat.request.ChatMessageRequest;
import com.mbproyect.campusconnect.dto.chat.response.ChatMessageResponse;
import com.mbproyect.campusconnect.service.chat.ChatMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.UUID;

/**
 *  Websocket controller for event chats
 */

@Slf4j
@Controller
public class EventChatWebsocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService messageService;

    public EventChatWebsocketController(
            SimpMessagingTemplate messagingTemplate,
           @Qualifier("eventChatMessageService") ChatMessageService messageService
    ) {
        this.messagingTemplate = messagingTemplate;
        this.messageService = messageService;
    }

    @MessageMapping("/event/{chatId}/send")
    public void sendMessage(
            @DestinationVariable UUID chatId,
            ChatMessageRequest messageRequest,
            Principal principal  // Provides the authentication state
    ) {
        ChatMessageResponse chatMessageResponse = messageService
                .sendMessage(messageRequest, chatId, principal.getName());

        // Sends the message to chatId subscribers
        log.info("Chat message sent");

        messagingTemplate.convertAndSend(
                "/event/chat/" + chatId, chatMessageResponse
        );
    }

}
