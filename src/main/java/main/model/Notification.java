package main.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Notification {
    @Id
    @Column(name = "notification_id")
    private String notificationId;

    private Integer value;

    @Column(name = "senderName")
    String senderName;

    @Column(name = "receiver_name")
    String receiverName;
}
