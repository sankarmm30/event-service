package com.takeaway.challenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableKafka
@EnableTransactionManagement
public class EventServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(EventServiceApp.class, args);
    }

}
