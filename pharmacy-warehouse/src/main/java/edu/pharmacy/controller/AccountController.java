package edu.pharmacy.controller;

import edu.pharmacy.model.dto.AccountFullInfoDTO;
import edu.pharmacy.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public AccountFullInfoDTO create(@RequestBody String accountJson) {
        return accountService.create(accountJson);
    }

    @GetMapping
    public List<AccountFullInfoDTO> getAll() {
        return accountService.getAll();
    }

    @GetMapping(value = "{id}")
    public AccountFullInfoDTO get(@PathVariable("id") String iban) {
        return accountService.get(iban);
    }

    @PutMapping(value = "{id}")
    public AccountFullInfoDTO update(@PathVariable("id") String iban, @RequestBody String updatedAccountJson) {
        return accountService.updateBalance(iban, updatedAccountJson);
    }

    @DeleteMapping(value = "{id}")
    public String delete(@PathVariable("id") String iban) {
        return accountService.delete(iban);
    }
}
