package main.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Auth {
    @Id
    String id;
    int count;
    @Column(name = "server_date_time")
    LocalDateTime localDateTime;

    @Column(name = "encrypted_email")
    String encryptedEmail;

}
