package main.service;

import main.dto.DtoMessage;
import main.dto.MessageMapper;
import main.model.Message;
import main.model.Status;
import main.model.User;
import main.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class MessageService {
    @Autowired
    MessageRepository messageRepository;

    public Message saveAndReturn(User sender, User receiver, String content) {
        Message msg = new Message();

        msg.setSenderId(sender.getId());
        msg.setSenderName(sender.getName());

        msg.setReceiverId(receiver.getId());
        msg.setReceiverName(receiver.getName());

        msg.setDateTime(LocalDateTime.now());
        msg.setContent(content);

        messageRepository.save(msg);
        return msg;
    }

    public void received(List<Message> messages, String senderName) {
        messages.stream()
                .filter(message -> Objects.equals(message.getReceiverName(), senderName))
                .forEach(message -> {
                    if (message.getStatus() != Status.RECEIVED) {
                        message.setStatus(Status.RECEIVED);
                        messageRepository.save(message);
                    }
                });
    }

    public List<DtoMessage> sort(List<Message> messages) {
        messages.sort(Comparator.comparing(Message::getDateTime));
        return messages.stream()
                .map(MessageMapper::map)
                .collect(Collectors.toList());
    }

}