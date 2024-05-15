package com.santeut.party.common.config;

import java.util.Map;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

public class ChattingHandshakeInterceptor implements HandshakeInterceptor {

  @Override
  public boolean beforeHandshake(org.springframework.http.server.ServerHttpRequest request,
      org.springframework.http.server.ServerHttpResponse response, WebSocketHandler wsHandler,
      Map<String, Object> attributes) throws Exception {
    String path = request.getURI().getPath();
    String roomId = path.substring(path.lastIndexOf('/') + 1);
    attributes.put("roomId", roomId);
    return true;
  }

  @Override
  public void afterHandshake(org.springframework.http.server.ServerHttpRequest request,
      org.springframework.http.server.ServerHttpResponse response, WebSocketHandler wsHandler,
      Exception exception) {

  }
}
