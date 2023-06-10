package com.alco.algorithmic.controllers;
import com.alco.algorithmic.entity.Account;
import com.alco.algorithmic.entity.Message;
import com.alco.algorithmic.dao.MessageRepository;
import com.alco.algorithmic.responseRequests.AccountNameRequest;
import com.alco.algorithmic.responseRequests.AccountStatusRequest;
import com.alco.algorithmic.responseRequests.MessageRequest;
import com.alco.algorithmic.service.AccountService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/user")
@Validated
public class UserController {
    private final AccountService accountService;

    @PostMapping("/name")
    public ResponseEntity<String> setName(@Valid @RequestBody AccountNameRequest request) {
        var account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(accountService.setName(account.getId(), request))
            return new ResponseEntity<>("OK", HttpStatus.OK);
        return new ResponseEntity<>("Cannot modify", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/status")
    public ResponseEntity<String> setStatus(@Valid @RequestBody AccountStatusRequest status) {
        var account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(accountService.setStatus(account.getId(), status.getStatus()))
            return new ResponseEntity<>("OK", HttpStatus.OK);
        return new ResponseEntity<>("Cannot modify", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
