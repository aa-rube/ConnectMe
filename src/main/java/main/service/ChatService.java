package main.service;

import main.model.Chat;
import main.model.Message;
import main.model.Status;
import main.model.User;
import main.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
public class ChatService {
    @Autowired
    ChatRepository chatRepository;

    public List<Chat> findByChatId(User sender, User receiver){
        return chatRepository.findByChatId(chatId(sender,receiver));
    }

    public void save(User sender, User receiver, Message msg) {
        List<Chat> chats = findByChatId(sender, receiver);
        Chat chat;

        if (chats.isEmpty()) {
            chat = new Chat();
            chat.setChatId(chatId(sender, receiver));
            chat.setSenderId(sender.getId());
            chat.setRecipientId(receiver.getId());
        } else {
            chat = chats.get(0);
        }

        msg.setStatus(Status.DELIVERED);
        chat.getListMessages().add(msg);
        chatRepository.save(chat);
    }

    public long chatId(User sender, User receiver) {
        if (receiver == null){
            return 0;
        }
        TreeSet<String> set = new TreeSet<>();
        set.add(sender.getEncryptedEmail());
        set.add(sender.getName());
        set.add(String.valueOf(sender.getId()));

        set.add(receiver.getEncryptedEmail());
        set.add(receiver.getName());
        set.add(String.valueOf(receiver.getId()));
        return String.join("&&",set).hashCode();
    }

    public List<Message> getListMessages(User sender, User receiver) {
        List<Chat> chats = findByChatId(sender, receiver);
        if (chats.isEmpty()) {
            return Collections.emptyList();
        }

        return chats.stream()
                .flatMap(chat -> chat.getListMessages().stream())
                .collect(Collectors.toList());
    }
}