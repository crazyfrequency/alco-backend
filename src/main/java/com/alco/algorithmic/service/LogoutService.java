package com.alco.algorithmic.service;

import com.alco.algorithmic.dao.TokenRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;

    @Value("${cookie.path}")
    private String cookiePath;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        var cookies = request.getCookies();
        if (cookies == null) return;
        var cookie = Arrays.stream(cookies).filter(c -> c.getName().equals("Auth-Key")).findFirst().orElse(null);
        if (cookie == null) return;
        final String jwt = cookie.getValue();
        var storedToken = tokenRepository.findByToken(jwt)
                .orElse(null);
        if (storedToken != null) {
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
            Cookie cookieClear = new Cookie("Auth-Key", null);
            cookieClear.setPath(cookiePath);
            cookieClear.setHttpOnly(true);
            cookieClear.setMaxAge(0);
            response.addCookie(cookieClear);
            SecurityContextHolder.clearContext();
        }
    }

}
