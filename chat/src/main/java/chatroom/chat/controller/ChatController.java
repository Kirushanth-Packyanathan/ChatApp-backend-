package chatroom.chat.controller;

import chatroom.chat.model.Message;
import chatroom.chat.model.UserData;
import chatroom.chat.repository.MessageRepository;
import chatroom.chat.repository.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserDataRepository userDataRepository;

    @MessageMapping("/message")
    public void receiveMessage(@Payload Message message) {
        // Save the message to the database
        messageRepository.save(message);
        System.out.println(message.toString());
        simpMessagingTemplate.convertAndSend("/chatroom/public", message);
    }

    @MessageMapping("/private-message")
    public void recMessage(@Payload Message message) {
        // Save the message to the database
        messageRepository.save(message);
        // Send private message to the recipient
        simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(), "/private", message);
        System.out.println(message.toString());
    }

    @MessageMapping("/messages")
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    @MessageMapping("/userdata")
    public UserData getUserData(@Payload String username) {
        Optional<UserData> userDataOptional = Optional.ofNullable(userDataRepository.findByUsername(username));
        return userDataOptional.orElseGet(() -> {
            // If user data does not exist, create and save new user data
            UserData newUser = new UserData();
            newUser.setUsername(username);
            return userDataRepository.save(newUser);
        });
    }

    @MessageMapping("/login")
    public void login(@Payload String username) {
        UserData userData = userDataRepository.findByUsername(username);
        if (userData == null) {
            userData = new UserData();
            userData.setUsername(username);
            userData.setConnected(true);
            // Handle first-time login logic if needed
        } else {
            // Retrieve stored messages associated with the user
            List<Message> messages = messageRepository.findBySenderName(username);
            if (!messages.isEmpty()) {
                // Send stored messages to the client
                messages.forEach(message -> simpMessagingTemplate.convertAndSendToUser(username, "/private", message));
            }
        }
        // Save or update user data in the database
        userDataRepository.save(userData);
    }
}
