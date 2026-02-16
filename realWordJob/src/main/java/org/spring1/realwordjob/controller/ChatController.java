package org.spring1.realwordjob.controller;

import org.spring1.realwordjob.model.ChatMessage;
import org.spring1.realwordjob.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatMessageService chatService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/send")
    public void sendMessage(@Payload ChatMessage message) {
        chatService.saveMessage(message);
        messagingTemplate.convertAndSend("/topic/messages/" + message.getReceiverEmail(), message);
    }

    @GetMapping("/history")
    public List<ChatMessage> getChatHistory(@RequestParam String email1, @RequestParam String email2) {
        return chatService.getMessagesBetweenEmails(email1, email2);
    }

    @PostMapping("/send-rest")
    public ChatMessage sendMessageRest(@RequestBody ChatMessage message) {
        ChatMessage saved = chatService.saveMessage(message);
        messagingTemplate.convertAndSend("/topic/messages/" + message.getReceiverEmail(), message);
        return saved;
    }
}
