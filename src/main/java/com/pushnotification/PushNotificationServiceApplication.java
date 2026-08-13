package com.pushnotification;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class PushNotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PushNotificationServiceApplication.class, args);
    }
}
