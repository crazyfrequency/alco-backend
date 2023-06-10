package com.alco.algorithmic.service;

import com.alco.algorithmic.dao.AccountRepository;
import com.alco.algorithmic.dao.TokenRepository;
import com.alco.algorithmic.entity.Account;
import com.alco.algorithmic.entity.File;
import com.alco.algorithmic.entity.Token;
import com.alco.algorithmic.enums.FriendStatus;
import com.alco.algorithmic.enums.Role;
import com.alco.algorithmic.enums.TokenType;
import com.alco.algorithmic.responseRequests.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {
    @Value("${application.security.jwt.expiration}")
    private int jwtExpiration;

    @Value("${cookie.path}")
    private String cookiePath;

    private final AccountRepository accountRepository;
    private final JwtService jwtService;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

//    public List<Dialog> getAccountDialogs(Long id){
//        return dialogRepository.findByUsersIn();
//    }

    public AccountResponseFull getAccountById(Long id) {
        Account account = accountRepository.getAccountById(id);
        return new AccountResponseFull(account);
    }

    public AccountResponse getAccountLiteById(Long id) {
        Account account = accountRepository.getAccountById(id);
        return new AccountResponse(account);
    }

    public List<AccountResponseFull> search(String text, Long offset) {
        if(offset==null)
            return accountRepository.findByNameAndSurname(text).stream().map(AccountResponseFull::new).toList();
        return accountRepository.findByNameAndSurnameWithOffset(text, offset).stream().map(AccountResponseFull::new).toList();
    }

    public List<AccountResponseFull> getFriendsByAccountId(Long id) {
        return accountRepository.getFriendsByUserId(id).stream().map(AccountResponseFull::new).collect(Collectors.toList());
    }

    public List<AccountResponseFull> getFriendsRequestsByAccountId(Long id) {
        return accountRepository.getFriendsRequestsByUserId(id).stream().map(AccountResponseFull::new).collect(Collectors.toList());
    }

    public List<AccountResponseFull> getMyFriendsRequestsByAccountId(Long id) {
        return accountRepository.getMyFriendsRequestsByUserId(id).stream().map(AccountResponseFull::new).collect(Collectors.toList());
    }

    public List<AccountResponseForAdmin> getAccounts() {
        return accountRepository.findAll().stream().map(AccountResponseForAdmin::new).collect(Collectors.toList());
    }

    public boolean setConfirmById(Long id) {
        return accountRepository.setConfirmById(id) == 1;
    }

    ;


    public void setAvatar(Long id, Long avatar) {
        accountRepository.setAvatarByUserId(id, avatar);
    }

    public String addFriend(Long id1, Long id2) {
        return accountRepository.addFriend(id1, id2, FriendStatus.INVITE.name());
    }

    @Transactional
    public boolean setName(Long id, AccountNameRequest request) {
        int res = 0;
        if(request.getFirstname()!=null && !request.getFirstname().isEmpty())
            res += accountRepository.setNameByUserId(id, request.getFirstname());
        if(request.getSurname()!=null && !request.getSurname().isEmpty())
            res += accountRepository.setSurnameByUserId(id, request.getSurname());
        return res!=0;
    }

    public boolean setStatus(Long id, String status) {
        return accountRepository.setStatusByUserId(id, status)!=0;
    }

    public AuthenticationResponse register(RegisterRequest request, @NonNull HttpServletResponse response) throws MessagingException {
        Account account = Account.builder()
                .name(request.getFirstname())
                .surname(request.getSurname())
                .email(request.getEmail())
                .indicator((short) 0)
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.NEW)
                .build();
        Account savedAccount = accountRepository.save(account);
        mailService.sendRegMail(request.getEmail(), savedAccount);
        return AuthenticationResponse.builder()
                .accessToken(jwtService.generateToken(savedAccount))
                .refreshToken(jwtService.generateRefreshToken(savedAccount))
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request, @NonNull HttpServletResponse response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        Account user = accountRepository.findByEmail(request.getEmail()).orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        Cookie cookie = new Cookie("Auth-Key", jwtToken);
        cookie.setHttpOnly(true);
        cookie.setPath(cookiePath);
        cookie.setMaxAge(jwtExpiration / 1000);
        response.addCookie(cookie);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(Account user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(Account user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) return;
        validUserTokens.forEach(token -> token.setRevoked(true));
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            Account user = accountRepository.findByEmail(userEmail)
                    .orElseThrow();
            if (!jwtService.isTokenValid(refreshToken, user)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid token");
            }
            String accessToken = jwtService.generateToken(user);
            revokeAllUserTokens(user);
            saveUserToken(user, accessToken);
            AuthenticationResponse authResponse = AuthenticationResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
            new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
        }
    }

}
