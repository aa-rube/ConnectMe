package app.repository;

import app.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {
    List<Notification> findAllByNotificationId(String notificationId);

    Optional<Notification> findByNotificationId(String notificationId);
}
