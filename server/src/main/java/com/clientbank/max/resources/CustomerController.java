package com.clientbank.max.resources;

import com.clientbank.max.entities.Account;
import com.clientbank.max.entities.Customer;
import com.clientbank.max.services.AccountService;
import com.clientbank.max.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final AccountService accountService;

    @GetMapping
    public List<Customer> findAll() {
        return customerService.findAll();
    }

    @GetMapping("/{id}")
    public Customer getOne(@PathVariable(name = "id") Long id) {
        return customerService.getOne(id);
    }

    @PostMapping
    public Customer save(@RequestBody Customer customer) {
        return customerService.save(customer);
    }

    @DeleteMapping("/{id}")
    public boolean deleteById(@PathVariable(name = "id") Long id) {
        return customerService.deleteById(id);
    }

    @PutMapping("/{id}")
    public Customer update(@RequestBody Customer customer) {
        return customerService.update(customer);
    }

    @PostMapping("/{id}/account")
    public Customer createAccount(
            @PathVariable(name = "id") Long id,
            @RequestBody Account account
    ) {
        return accountService.createAccount(id, account);
    }

    @DeleteMapping("/{id}/account")
    public boolean deleteAccountById(
            @PathVariable(name = "id") Long customerId,
            @RequestParam(value = "number") String number
    ) {
        return accountService.deleteAccount(number, customerId);
    }

    @PostMapping("{customerId}/employer/{employerId}")
    public boolean addEmployer (
            @PathVariable (name = "customerId") Long customerId,
            @PathVariable (name = "employerId") Long employerId
    ) {
        return customerService.addEmployer(customerId, employerId);
    }
}
