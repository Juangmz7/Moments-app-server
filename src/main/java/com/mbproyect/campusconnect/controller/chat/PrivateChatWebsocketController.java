package com.mbproyect.campusconnect.controller.chat;

import com.mbproyect.campusconnect.dto.chat.request.ChatMessageRequest;
import com.mbproyect.campusconnect.dto.chat.response.ChatMessageResponse;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.UUID;

/**
 *  Web socket controller for private chats
 */

@Controller
public class PrivateChatController {

    private final SimpMessagingTemplate messagingTemplate;

    public PrivateChatController(
            SimpMessagingTemplate messagingTemplate
    ) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/user/{chatId}/send")
    public void sendMessage(
            @DestinationVariable UUID chatId,
            ChatMessageRequest messageRequest
    ) {
        ChatMessageResponse chatMessageResponse = new ChatMessageResponse();

        // Sends the message to chatId subscribers
        messagingTemplate.convertAndSend(
                "/event/chat/" + chatId, chatMessageResponse
        );
    }
}
