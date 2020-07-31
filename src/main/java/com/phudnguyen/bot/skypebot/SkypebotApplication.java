package com.phudnguyen.bot.skypebot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SkypebotApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkypebotApplication.class, args);
    }

}
