package ru.gb.dev.spring.pfs.accounting.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gb.dev.spring.pfs.accounting.model.dto.AccountDto;
import ru.gb.dev.spring.pfs.accounting.model.dto.util.ResultDto;
import ru.gb.dev.spring.pfs.accounting.model.dto.util.SuccessDto;
import ru.gb.dev.spring.pfs.accounting.model.entity.Account;
import ru.gb.dev.spring.pfs.accounting.model.service.AccountService;
import ru.gb.dev.spring.pfs.accounting.exception.EntityNotFoundException;


import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/api/accounts")
public class AccountsController {

    private final AccountService service;

    private final ModelMapper modelMapper;

    @Autowired
    public AccountsController(final AccountService service, final ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @GetMapping(value = "/ping", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResultDto ping() {
        return new SuccessDto();
    }

    @GetMapping(value = "{id}", produces = APPLICATION_JSON_UTF8_VALUE)
    public AccountDto getAccount(@PathVariable("id") final String id) {
        return service.findById(id)
                .map(account -> modelMapper.map(account, AccountDto.class))
                .orElseThrow(() -> new EntityNotFoundException("Account with id " + id + "not found"));
    }
    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE)
    public List<AccountDto> getListAccount() {
        final Iterable<Account> accounts = service.findAll();
        return StreamSupport
                .stream(accounts.spliterator(), false)
                .map(account -> modelMapper.map(account, AccountDto.class))
                .collect(Collectors.toList());
    }

    @PostMapping(produces = APPLICATION_JSON_UTF8_VALUE)
    public ResultDto post(final AccountDto accountDto) {
        service.save(accountDto);
        return new ResultDto();
    }

    @PutMapping(produces = APPLICATION_JSON_UTF8_VALUE)
    public ResultDto put(final AccountDto accountDto) {
        service.save(accountDto);
        return new ResultDto();
    }

    @DeleteMapping(produces = APPLICATION_JSON_UTF8_VALUE)
    public ResultDto deleteAccount(final AccountDto accountDto) {
        service.delete(accountDto);
        return new ResultDto();
    }

    @DeleteMapping(
            consumes = APPLICATION_JSON_UTF8_VALUE,
            produces = APPLICATION_JSON_UTF8_VALUE
    )
    public ResultDto deleteAccounts(final AccountDto[] accounts) {
        for (final AccountDto account : accounts) {
            service.delete(account);
        }
        return new ResultDto();
    }

}
