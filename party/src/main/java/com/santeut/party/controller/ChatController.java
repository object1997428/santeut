package com.santeut.party.controller;

import com.santeut.party.common.response.BasicResponse;
import com.santeut.party.common.util.ResponseUtil;
import com.santeut.party.service.ChatService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

  private final ChatService chatService;

  @GetMapping()
  public ResponseEntity<BasicResponse> findMyChatRoom(HttpServletRequest request) {
    return ResponseUtil.buildBasicResponse(HttpStatus.OK, chatService.findMyChatRoom(Integer.parseInt(request.getHeader("userId"))));
  }

  @GetMapping("/{partyId}")
  public ResponseEntity<BasicResponse> getAllChatMessages(HttpServletRequest request,
      @PathVariable("partyId") int partyId) {
    return ResponseUtil.buildBasicResponse(HttpStatus.OK,
        chatService.getAllChatMessage(Integer.parseInt(request.getHeader("userId")),
            partyId));
  }

}
