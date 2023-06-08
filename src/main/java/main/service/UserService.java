package main.service;

import main.model.User;
import main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.*;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public String sessionId() {
        return RequestContextHolder.currentRequestAttributes().getSessionId();
    }
    public boolean save(String name) {
        User user = new User();
        user.setSessionId(sessionId());
        user.setName(name);
        userRepository.save(user);
        return true;
    }

    public void save(User user){
        userRepository.save(user);
    }

    public Optional<User> findOPtBySessionId() {
        return userRepository.findBySessionId(sessionId());
    }

    public Optional<User> findByEncryptedEmail(String encryptedEmail){
        return userRepository.findByEncryptedEmail(encryptedEmail);
    }

    public Optional<User> findOptionalByName(String name){
        return userRepository.findOptionalByName(name);
    }

    public User findBySessionId() {
        if (userRepository.findBySessionId(sessionId()).isPresent()) {
            return userRepository.findBySessionId(sessionId()).get();
        }
        return new User();
    }

    public User findByName(String name) {
        return userRepository.findByName(name);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }
}
