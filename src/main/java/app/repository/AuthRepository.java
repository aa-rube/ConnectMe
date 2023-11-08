package app.repository;

import app.model.Auth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<Auth,Integer> {
    Optional<Auth> findById(String sessionId);
}
