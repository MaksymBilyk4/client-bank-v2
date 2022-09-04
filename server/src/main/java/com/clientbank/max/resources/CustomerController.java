package com.clientbank.max.resources;

import com.clientbank.max.entities.Customer;
import com.clientbank.max.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public List<Customer> findAll () {
        return customerService.findAll();
    }

    @GetMapping("/{id}")
    public Customer getOne(@PathVariable (name = "id") Long id) {
        return customerService.getOne(id);
    }

    @PostMapping
    public Customer save(@RequestBody Customer customer) {
        return customerService.save(customer);
    }

    @DeleteMapping("/{id}")
    public boolean deleteById (@PathVariable (name = "id") Long id) {
        return customerService.deleteById(id);
    }
}
