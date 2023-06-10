package com.alco.algorithmic.config;

import com.alco.algorithmic.enums.Permission;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static com.alco.algorithmic.enums.Role.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig {


    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain web(HttpSecurity http, AuthenticationProvider authenticationProvider, LogoutHandler logoutHandler, JwtAuthenticationFilter jwtAuthFilter) throws Exception {
        http.csrf().disable()
                .cors().disable()
                .authorizeHttpRequests()
                .shouldFilterAllDispatcherTypes(false)
                .requestMatchers("/login", "/refresh-token", "/register", "/confirm/**").permitAll()
                .requestMatchers("/check").authenticated()
                .requestMatchers("/users/me").hasAnyRole(USER.name(), ADMIN.name())
                .requestMatchers("/users").hasRole(ADMIN.name())
                .requestMatchers("/posts/my", "/posts/lent").authenticated()
                .requestMatchers(HttpMethod.GET, "/users/**", "posts/**").permitAll()
                .requestMatchers("/posts/**").hasAnyAuthority(Permission.SELF_UPDATE.name())
                .requestMatchers("/avatars/**").permitAll()
                .requestMatchers("/").permitAll()
                .requestMatchers("/status**", "/topic/*","/ws/**", "/chat").permitAll()
                .requestMatchers(HttpMethod.GET, "/comments/**", "/files/**", "/friends/**").permitAll()
                .requestMatchers("/**", "/friends/requests").hasAnyRole(USER.name(), ADMIN.name())
                .anyRequest().hasAnyRole(USER.name(), ADMIN.name())
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout().logoutUrl("/logout").addLogoutHandler(logoutHandler).logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext());
        return  http.build();
    }

}
