package app.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "session_id", columnDefinition = "TEXT")
    private String sessionId;

    private String name;

    @Column(name = "encrypted_email")
    private String encryptedEmail;

    private int status;

    @Column(name = "last_action")
    LocalDateTime lastAction;

    public Integer getId() {
        return id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getName() {
        return name;
    }

    public String getEncryptedEmail() {
        return encryptedEmail;
    }

    public int getStatus() {
        return status;
    }

    public LocalDateTime getLastAction() {
        return lastAction;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEncryptedEmail(String encryptedEmail) {
        this.encryptedEmail = encryptedEmail;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setLastAction(LocalDateTime lastAction) {
        this.lastAction = lastAction;
    }
}