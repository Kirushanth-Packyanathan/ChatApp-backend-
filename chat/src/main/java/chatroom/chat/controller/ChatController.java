package chatroom.chat.controller;

import chatroom.chat.model.Message;
import chatroom.chat.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import java.util.List;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private MessageRepository messageRepository;

    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public Message receiveMessage(@Payload Message message) {
        // Save the message to the database
        messageRepository.save(message);
        System.out.println(message.toString());
        return message;
    }

    @MessageMapping("/private-message")
    public Message recMessage(@Payload Message message){
        // Save the message to the database
        messageRepository.save(message);
        // Send private message to the recipient
        simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(), "/private", message);
        System.out.println(message.toString());
        return message;
    }

    // Endpoint to retrieve all messages from the database
    @MessageMapping("/messages")
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }
}
