package app.service;

import app.model.Auth;
import app.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private AuthRepository authRepository;

    public Optional<Auth> findById(String s) {
        return authRepository.findById(s);
    }

    public boolean isScam(String sessionId, String email) {
        Optional<Auth> optionalAuth = findById(sessionId);
        Auth auth = new Auth();

        if (optionalAuth.isPresent()) {
            auth = optionalAuth.get();
            auth.setCount(auth.getCount() +1);
            auth.setLocalDateTime(LocalDateTime.now());
            auth.setEncryptedEmail(email);
        } else {
            auth.setId(sessionId);
            auth.setCount(1);
            auth.setLocalDateTime(LocalDateTime.now());
            auth.setEncryptedEmail(email);

        }
        authRepository.save(auth);
        return auth.getCount() > 10;
    }
}