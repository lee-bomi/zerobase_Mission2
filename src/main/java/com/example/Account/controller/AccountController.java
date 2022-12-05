package com.example.Account.controller;

import com.example.Account.domain.Account;
import com.example.Account.dto.AccountDto;
import com.example.Account.dto.AccountInfo;
import com.example.Account.dto.CreateAccount;
import com.example.Account.dto.DeleteAccount;
import com.example.Account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;


    @PostMapping("/account")
    public CreateAccount.Response createAccount(@RequestBody @Valid CreateAccount.Request request) {
        AccountDto accountDto = accountService.createAccount(request.getUserId(), request.getInitialBalance());
        return CreateAccount.Response.from(accountDto);
    }

    @DeleteMapping("/account")
    public DeleteAccount.Response deleteAccount(@RequestBody @Valid DeleteAccount.Request request) {
        AccountDto accountDto = accountService.deleteAccount(request.getUserId(), request.getAccountNumber());
        return DeleteAccount.Response.from(accountDto);
    }

    @GetMapping("/account")
    public List<AccountInfo> getAccountByUserId(@RequestParam("user_id") Long user_id) {
        return accountService.getAccountsByUserId(user_id).stream()
                .map(AccountDto ->
                        AccountInfo.builder()
                        .accountNumber(AccountDto.getAccountNumber())
                        .balance(AccountDto.getBalance())
                        .build())
                .collect(Collectors.toList());
    }


}
