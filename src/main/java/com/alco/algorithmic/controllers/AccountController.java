package com.alco.algorithmic.controllers;

import com.alco.algorithmic.entity.Account;
import com.alco.algorithmic.entity.Post;
import com.alco.algorithmic.responseRequests.AccountResponse;
import com.alco.algorithmic.responseRequests.AccountResponseForAdmin;
import com.alco.algorithmic.responseRequests.AccountResponseFull;
import com.alco.algorithmic.service.AccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class AccountController {

    @Autowired
    private final AccountService accountService;

    @GetMapping("/me")
    public AccountResponseFull getMyUser() {
        var account = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new AccountResponseFull((Account) account);
    }

    @GetMapping("/search")
    public List<AccountResponseFull> findUsers(@RequestParam(name = "text") String text, @RequestParam(name="offset", required = false) Long offset) {
        return accountService.search(text, offset);
    }

    @GetMapping("/{id}")
    public AccountResponseFull getUser(@PathVariable Long id) {
        return accountService.getAccountById(id);
    }

    @GetMapping("/{id}/lite")
    public AccountResponse getUserLite(@PathVariable Long id) {
        return accountService.getAccountLiteById(id);
    }

    @GetMapping()
    public List<AccountResponseForAdmin> getUsers() {

        return accountService.getAccounts();
    }

    @PostMapping("/me/avatar/{id}")
    public ResponseEntity<String> setAvatar(@PathVariable Long id) throws Exception {
        var account = (Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        accountService.setAvatar(account.getId(), id);
        return ResponseEntity.ok("OK");
    }


    @GetMapping("/{id}/lent")
    public List<Post> getLentPosts(@PathVariable Long id) {
        return null;
    }

}
