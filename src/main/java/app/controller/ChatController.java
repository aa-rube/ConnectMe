package app.controller;

import app.dto.DtoMessage;
import app.model.Message;
import app.model.User;
import app.service.NotificationService;
import app.service.ChatService;
import app.service.MessageService;
import app.service.UserService;
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
                                            @RequestParam String userName) {
        if (message.isEmpty() || userName.isEmpty()) {
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
        Optional<User> senderOpt = userService.findOptBySessionId();

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
        Optional<User> userOpt = userService.findOptBySessionId();

        if (userOpt.isPresent() && userService.findAll().size() > 1) {
           return notificationService.getMapWithCountNotification(userOpt.get());
        }
        return new HashMap<>();
    }

    @GetMapping("/status")
    public Map<String, Integer> getStatuses(){
        return Map.of("", 0);
    }
}