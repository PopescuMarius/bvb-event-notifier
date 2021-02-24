package com.scopert.bvbeventnotifier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

//TODO deploy in AWS ?!
@SpringBootApplication
@EnableScheduling
public class BvbEventNotifierApplication {

    public static void main(String[] args) {
        SpringApplication.run(BvbEventNotifierApplication.class, args);
    }

}
