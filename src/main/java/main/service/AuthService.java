package main.service;

import main.model.Auth;
import main.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private AuthRepository authRepository;

    public Optional<Auth> findById(String s) {
        return authRepository.findById(s);
    }

    public boolean isScam(String sessionId) {
        Optional<Auth> optionalAuth = findById(sessionId);
        Auth auth;

        if (optionalAuth.isPresent()) {
            auth = optionalAuth.get();
            auth.setCount(auth.getCount() +1);
        } else {
            auth = new Auth();
            auth.setId(sessionId);
            auth.setCount(1);
        }
        authRepository.save(auth);
        return auth.getCount() > 3;
    }
}
