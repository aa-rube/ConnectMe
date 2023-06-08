package main.controller;

import main.dto.DtoMessage;
import main.model.Message;
import main.model.User;
import main.service.NotificationService;
import main.service.ChatService;
import main.service.MessageService;
import main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class ChatController {
    @Autowired
    private ChatService chatService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserService userService;

    @PostMapping("/message")
    public Map<String, Boolean> sendMessage(@RequestParam String message,
                                            @RequestParam String userName) {//receiver
        if (message.length() == 0 || userName.length() == 0) {
            return Map.of("result", false);
        }
        User sender = userService.findBySessionId();
        User receiver = userService.findByName(userName);
        Message msg = messageService.saveAndReturn(sender, receiver, message);
        chatService.save(sender, receiver, msg);

        notificationService.add(sender, receiver);
        return Map.of("result", true);
    }

    @GetMapping("/message")
    public List<DtoMessage> getChatMessagesList(@RequestParam String userName) {
        Optional<User> senderOpt = userService.findOPtBySessionId();

        if (senderOpt.isPresent()) {
            User sender = senderOpt.get();
            User receiver = userService.findByName(userName);

            List<Message> messages = chatService.getListMessages(sender, receiver);
            messageService.received(messages, sender.getName());

            notificationService.delete(sender, receiver);
            return messageService.sort(messages);
        }
        return Collections.emptyList();
    }

    @GetMapping("/user")
    public Map<String, Integer> getUsersList() {
        Optional<User> userOpt = userService.findOPtBySessionId();

        if (userOpt.isPresent() && userService.findAll().size() > 1) {
           return notificationService.getMapWithCountNotification(userOpt.get());
        }
        return new HashMap<>();
    }
}