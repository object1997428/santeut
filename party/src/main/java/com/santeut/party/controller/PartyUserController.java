package com.santeut.party.controller;

import com.santeut.party.common.response.BasicResponse;
import com.santeut.party.common.util.ResponseUtil;
import com.santeut.party.service.PartyUserService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PartyUserController {

  private final PartyUserService partyUserService;

  @PostMapping("/user/join")
  public ResponseEntity<BasicResponse> joinParty(@RequestHeader("userLoginId") int userId,
      @RequestBody Map<String, Integer> map) {
    partyUserService.joinUserParty(userId, map.get("partyId"));
    return ResponseUtil.buildBasicResponse(HttpStatus.OK, "소모임 가입 완료");
  }

}
