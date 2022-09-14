package com.clientbank.max;

import com.clientbank.max.dao.CustomerDao;
import com.clientbank.max.entities.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;

@EnableTransactionManagement
@SpringBootApplication
public class MaxApplication implements ApplicationRunner {
    public static void main(String[] args) {
        SpringApplication.run(MaxApplication.class, args);
    }

    @Autowired
    CustomerDao customerDao;

    @Override
    public void run(ApplicationArguments args) {
//        List<Customer> customers = customerDao.findAll();
        Customer customer = customerDao.getOne(21L);
        System.out.println(customer);
    }
}
