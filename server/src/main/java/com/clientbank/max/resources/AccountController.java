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

    @PutMapping("/transfer")
    public boolean transferMoney(
            @RequestParam(value = "fromNumber") String fromNumber,
            @RequestParam(value = "toNumber") String toNumber,
            @RequestParam(value = "amount") Double amount
    ) {
        return accountService.transferMoney(fromNumber, toNumber, amount);
    }

    @PutMapping("/withdraw")
    public boolean withdrawMoney(
            @RequestParam(value = "number") String number,
            @RequestParam(value = "amount") Double amount
    ) {
        return accountService.withdrawMoney(number, amount);
    }

    @PutMapping("/up")
    public boolean upMoney(
            @RequestParam(value = "number") String number,
            @RequestParam(value = "amount") Double amount
    ) {
        return accountService.toUpAccount(number, amount);
    }
}
