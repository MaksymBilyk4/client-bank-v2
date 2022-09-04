package com.clientbank.max.resources;

import com.clientbank.max.entities.Account;
import com.clientbank.max.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public List<Account> findAll() {
        return accountService.findAll();
    }

    @GetMapping("/{id}")
    public Account getOne(@PathVariable(name = "id") Long id) {
        return accountService.getOne(id);
    }

    @PostMapping
    public Account save(@RequestBody Account account) {
        return accountService.save(account);
    }

    @DeleteMapping("/{id}")
    public boolean deleteById(@PathVariable(name = "id") Long id) {
        return accountService.deleteById(id);
    }

}
