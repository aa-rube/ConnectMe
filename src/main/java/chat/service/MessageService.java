package chat.service;

import chat.dto.DtoMessage;
import chat.dto.MessageMapper;
import chat.model.Message;
import chat.model.MessageStatus;
import chat.model.User;
import chat.repository.MessageRepository;
import chat.repository.UserRepository;
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

    @Autowired
    UserRepository userRepository;

    public Message saveAndReturn(User sender, User receiver, String content) {
        sender.setStatus(1);
        sender.setLastAction(LocalDateTime.now());
        userRepository.save(sender);

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
                    if (message.getMessageStatus() != MessageStatus.RECEIVED) {
                        message.setMessageStatus(MessageStatus.RECEIVED);
                        messageRepository.save(message);

                        User u = userRepository.findByName(message.getReceiverName());
                        u.setStatus(1);
                        userRepository.save(u);
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