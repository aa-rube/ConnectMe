package main.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @Column(length = 9_999_999)
    private String content;

    @Column(name = "sender_id")
    private Integer senderId;

    @Column(name = "recipient_id")
    private Integer receiverId;

    @Column(name = "sender_name")
    private String senderName;

    @Column(name = "recipient_name")
    private String receiverName;

    private MessageStatus messageStatus;

}
