package com.scopert.bvbeventnotifier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

//TODO deploy in AWS ?! dupa ce am ceva care ruleaza pe local 1 saptamana fara erori
@SpringBootApplication
@EnableScheduling
public class BvbEventNotifierApplication {

    public static void main(String[] args) {
        SpringApplication.run(BvbEventNotifierApplication.class, args);
    }

}
