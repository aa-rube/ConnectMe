package main.repository;

import main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer > {
    Optional<User> findBySessionId(String sessionId);
    User findByName(String userName);

    Optional<User> findByEncryptedEmail(String encryptedEmail);

    Optional<User> findOptionalByName(String name);
}