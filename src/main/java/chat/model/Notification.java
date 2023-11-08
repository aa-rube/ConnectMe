package chat.model;

import jakarta.persistence.*;

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

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
}
