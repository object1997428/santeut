package com.santeut.party.controller;

import com.santeut.party.common.response.BasicResponse;
import com.santeut.party.common.util.ResponseUtil;
import com.santeut.party.dto.request.CreatePartyRequestDto;
import com.santeut.party.dto.request.ModifyPartyRequestDto;
import com.santeut.party.service.PartyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PartyController {

  private final PartyService partyService;

  @PostMapping("/")
  public ResponseEntity<BasicResponse> createParty(@RequestHeader("userLoginId") int userId,
      @RequestBody CreatePartyRequestDto requestDto) {
    partyService.createParty(userId, requestDto);
    return ResponseUtil.buildBasicResponse(HttpStatus.CREATED, "소모임 생성 성공");
  }

  @PatchMapping("/{partyId}")
  public ResponseEntity<BasicResponse> modifyParty(@RequestHeader("userLoginId") int userId,
      @PathVariable("partyId") int partyId, @RequestBody ModifyPartyRequestDto requestDto) {
    return ResponseUtil.buildBasicResponse(HttpStatus.OK,
        partyService.modifyParty(userId, partyId, requestDto));
  }


}
