package main.service;

import main.model.Notification;
import main.model.User;
import main.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    UserService userService;

    public String notifyId(User sender, User receiver) {
        TreeSet<String> set = new TreeSet<>();
        set.add(sender.getName());
        set.add(receiver.getName());
        return String.join("&", set);
    }
    public void add(User sender, User receiver) {
        Notification entity = new Notification();
        entity.setNotificationId(notifyId(sender, receiver));

        entity.setSenderName(sender.getName());
        entity.setReceiverName(receiver.getName());
        entity.setValue(1);
        notificationRepository.save(entity);
    }

    public void delete(User sender, User receiver) {
        String notificationId = notifyId(sender, receiver);
        List<Notification> notificationsToDelete = notificationRepository.findAllByNotificationId(notificationId);
        notificationsToDelete.stream()
                .filter(notify -> !notify.getSenderName().equals(sender.getName()) &&
                        notify.getReceiverName().equals(sender.getName()))
                .forEach(notificationRepository::delete);
    }


    public Map<String, Integer> getMapWithCountNotification(User owner) {
        Map<String, Integer> newMap = new HashMap<>();

        for(User other : userService.findAll()) {
            Optional<Notification> notify = notificationRepository.findByNotificationId(notifyId(owner, other));

            if(notify.isEmpty()) {
                newMap.put(other.getName(),0);
                continue;
            }

            if(owner.getName().equals(notify.get().getSenderName())){
                newMap.put(other.getName(),0);
                continue;
            }

            newMap.put(other.getName(), 1);
        }

        newMap.remove(owner.getName());
        return newMap;
    }
}