package main.controller;

import main.model.User;
import main.service.AuthService;
import main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
public class UserController {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;

    @GetMapping("/init")
    public Map<String, Boolean> init() {
        return Map.of("result", userService.findOPtBySessionId().isPresent());
    }

    @PostMapping("/auth")
    public Map<String, Boolean> auth(@RequestParam String encryptedEmail) {
        if(authService.isScam(userService.sessionId())){
            return Map.of("result", false);
        }

        if (userService.findByEncryptedEmail(encryptedEmail).isPresent()) {
            User user = userService.findByEncryptedEmail(encryptedEmail).get();
            user.setSessionId(userService.sessionId());
            userService.save(user);
            return Map.of("result", true);
        }

        return Map.of("result", false);
    }

    @PostMapping("/new_account")
    public Map<String, Boolean> createNewAccount(@RequestParam String encryptedEmail,
                                                 @RequestParam String username) {
        if(userService.findOptionalByName(username).isPresent()||
                userService.findByEncryptedEmail(encryptedEmail).isPresent()){
            return Map.of("result", false);
        }
        User user = new User();
        user.setEncryptedEmail(encryptedEmail);
        user.setSessionId(userService.sessionId());
        user.setName(username);
        userService.save(user);
        return Map.of("result", true);
    }
}