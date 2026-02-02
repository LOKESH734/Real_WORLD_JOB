package org.spring1.realwordjob.service;

import org.spring1.realwordjob.model.ChatMessage;
import org.spring1.realwordjob.repository.ChatMessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ChatMessageService {

    @Autowired
    private ChatMessageRepo chatMessageRepo;

    public ChatMessage saveMessage(ChatMessage message) {
        return chatMessageRepo.save(message);
    }

    public List<ChatMessage> getMessagesBetweenEmails(String email1, String email2) {
        return chatMessageRepo.findBySenderEmailAndReceiverEmailOrReceiverEmailAndSenderEmailOrderByIdAsc(
                email1, email2, email1, email2
        );
    }
}
