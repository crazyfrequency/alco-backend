package com.alco.algorithmic.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(final MessageBrokerRegistry config) {
//        config.enableSimpleBroker( "/hello");
//        config.setApplicationDestinationPrefixes("/app");
//        config.enableSimpleBroker("/topic");
//        config.setApplicationDestinationPrefixes("/app");
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(final StompEndpointRegistry registry) {
//        registry.addEndpoint("/hello");
//        registry.addEndpoint("/hello").withSockJS();
//        registry.addEndpoint("/topic/*");
//        registry.addEndpoint("/topic/*").withSockJS();
//        registry.addEndpoint("/ws").withSockJS();
        registry.addEndpoint("/chat", "/topic/greetings");
        registry.addEndpoint("/chat", "/topic/greetings").withSockJS();
    }

}
