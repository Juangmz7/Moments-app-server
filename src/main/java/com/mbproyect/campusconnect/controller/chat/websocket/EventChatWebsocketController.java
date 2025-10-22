package com.mbproyect.campusconnect.controller.chat.websocket;

import com.mbproyect.campusconnect.dto.chat.request.ChatMessageRequest;
import com.mbproyect.campusconnect.dto.chat.response.ChatMessageResponse;
import com.mbproyect.campusconnect.service.chat.ChatMessageService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.UUID;

/**
 *  Websocket controller for event chats
 */

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
            ChatMessageRequest messageRequest
    ) {
        ChatMessageResponse chatMessageResponse = messageService.sendMessage(messageRequest, chatId);

        // Sends the message to chatId subscribers
        messagingTemplate.convertAndSend(
                "/event/chat/" + chatId, chatMessageResponse
        );
    }

}
