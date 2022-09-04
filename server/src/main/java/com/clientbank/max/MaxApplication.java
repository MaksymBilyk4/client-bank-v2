package com.clientbank.max;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

@EnableTransactionManagement
@SpringBootApplication
public class MaxApplication implements ApplicationRunner {
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    public static void main(String[] args) {
        SpringApplication.run(MaxApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
    }
}
