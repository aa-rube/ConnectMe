package app.service;

import app.model.User;
import app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public void createUser(String encryptedEmail, String username) {
        User user = new User();
        user.setEncryptedEmail(encryptedEmail);
        user.setSessionId(sessionId());
        user.setName(username);
        user.setStatus(1);
        user.setLastAction(LocalDateTime.now());
        save(user);
    }

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

    public Optional<User> findOptBySessionId() {
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

    public Map<String, Integer> userStatus(User owner) {
        LocalDateTime now;
        List<User> list = userRepository.findAll();
        Map<String, Integer> statusesList = new HashMap<>();

        for (User u : list) {
            now = LocalDateTime.now();
            if (Duration.between(u.getLastAction(), now).toMinutes() > 15) {
                u.setStatus(0);
                statusesList.put(u.getName(), 0);
                userRepository.save(u);
            } else {
                statusesList.put(u.getName(), 1);
            }
        }
        statusesList.remove(owner.getName());
        return statusesList;
    }
}