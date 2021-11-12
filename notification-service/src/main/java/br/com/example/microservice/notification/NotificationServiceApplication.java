package br.com.example.microservice.notification;

import java.util.function.Consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;

@SpringBootApplication
public class NotificationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationServiceApplication.class, args);
	}
	
	 @Bean
	 /*The name of this method need to be the same defined in configuration yml
	 /cloud:
		    stream:
		    	
		      source: notificationEventSupplier
		      bindings:
		        notificationEventSupplier-out-0:
		          destination: notification-events 
		        notificationEventSupplier-in-0:
		          destination: notification-events
     */
	 public Consumer<Message<String>> notificationEventSupplier() {
	        return message -> new NotificationSender().sendNotification(message.getPayload());
	 }

}
