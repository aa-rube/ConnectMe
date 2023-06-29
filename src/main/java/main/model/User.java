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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "session_id")
    private String sessionId;


    private String name;

    @Column(name = "encrypted_email")
    private String encryptedEmail;

    private int status;

    @Column(name = "last_action")
    LocalDateTime lastAction;
}
