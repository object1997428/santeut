package com.santeut.hiking.common.config;

import com.santeut.hiking.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Configuration
@EnableWebSocket
@Slf4j
public class WebSocketConfig implements WebSocketConfigurer {
    //    private final WebSocketHandler webSocketHandler;
//private final RoomRepository roomRepository; // RoomRepository 의존성 주입
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(socketTextHandler(), "/chat/rooms/*")
                .addInterceptors(handshakeInterceptor())
                .setAllowedOrigins("*");
    }
    @Bean
    public HandshakeInterceptor handshakeInterceptor() {
        return new ChattingHandshakeInterceptor();
    }
    @Bean
    public SocketTextHandler socketTextHandler() {
        return new SocketTextHandler();
    }
}