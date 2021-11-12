package br.com.example.microservice.notification;

import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class NotificationSender {

    public void sendNotification(String message) {
        log.info("Notification send: {}", message);
    }
}