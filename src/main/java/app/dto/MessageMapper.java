package app.dto;

import app.model.Message;

import java.time.format.DateTimeFormatter;

public class MessageMapper {
    public static DtoMessage map(Message message) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy HH:mm");

        return new DtoMessage(
                message.getContent(),
                message.getDateTime().format(formatter),
                message.getSenderName(),
                String.valueOf(message.getMessageStatus()));
    }
}