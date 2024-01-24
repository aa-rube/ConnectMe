package app.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "session_id", columnDefinition = "LONGTEXT")
    private String sessionId;

    private String name;

    @Column(name = "encrypted_email")
    private String encryptedEmail;

    private int status;

    @Column(name = "last_action")
    private LocalDateTime lastAction;

    @Column(name = "auth_secure")
    private String authSecure;

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getAuthSecure() {
        return authSecure;
    }

    public void setAuthSecure(String email, String secret) {
        this.authSecure = email + ":" + secret;
    }

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

    public void setPrivateAuthKey(String sessionId) {
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