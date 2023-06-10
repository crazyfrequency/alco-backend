package com.alco.algorithmic.controllers;

import com.alco.algorithmic.dao.ConfirmCodeRepository;
import com.alco.algorithmic.entity.Account;
import com.alco.algorithmic.enums.Role;
import com.alco.algorithmic.responseRequests.AuthenticationRequest;
import com.alco.algorithmic.responseRequests.AuthenticationResponse;
import com.alco.algorithmic.responseRequests.RegisterRequest;
import com.alco.algorithmic.service.AccountService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Validated
public class AuthController {
    private final AccountService service;
    private final ConfirmCodeRepository confirmCodeRepository;

    @PostMapping("/register")
    public ResponseEntity register(@Valid @RequestBody RegisterRequest request, @NonNull HttpServletResponse response) throws MessagingException {
        service.register(request, response);
        return new ResponseEntity("Accepted", HttpStatus.ACCEPTED);
    }

    @PostMapping("/login")
    public AuthenticationResponse authenticate(@Valid @RequestBody AuthenticationRequest request, @NonNull HttpServletResponse response) {
        return service.authenticate(request, response);
    }

    @GetMapping("/check")
    public Object check() {
        var account = (Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return account.getRole().name();
    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        service.refreshToken(request, response);
    }

    @GetMapping("/confirm/{key}")
    public ResponseEntity confirm(@PathVariable String key) {
        var code = confirmCodeRepository.findByKey(key);
        if (code == null) return new ResponseEntity("Incorrect code", HttpStatus.CONFLICT);
        if (!service.setConfirmById(code.getUser().getId()))
            return new ResponseEntity("Incorrect code", HttpStatus.CONFLICT);
        return new ResponseEntity("OK", HttpStatus.OK);
    }
}
