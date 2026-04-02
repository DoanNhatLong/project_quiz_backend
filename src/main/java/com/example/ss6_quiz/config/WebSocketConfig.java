package com.example.ss6_quiz.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // 👉 endpoint để React connect
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS(); // fallback (khuyên dùng)
    }

    // 👉 cấu hình broker
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        // client gửi lên
        registry.setApplicationDestinationPrefixes("/app");

        // server broadcast xuống
        registry.enableSimpleBroker("/topic", "/queue");

        // gửi riêng user
        registry.setUserDestinationPrefix("/user");
    }
}