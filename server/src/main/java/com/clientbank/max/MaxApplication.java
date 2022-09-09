package com.clientbank.max;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class MaxApplication implements ApplicationRunner {
    public static void main(String[] args) {
        SpringApplication.run(MaxApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {}
}
