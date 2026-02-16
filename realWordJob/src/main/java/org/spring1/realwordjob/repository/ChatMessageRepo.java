
package org.spring1.realwordjob.repository;

import org.spring1.realwordjob.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ChatMessageRepo extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findBySenderEmailAndReceiverEmailOrReceiverEmailAndSenderEmailOrderByIdAsc(
            String senderEmail, String receiverEmail, String senderEmail2, String receiverEmail2);
}

