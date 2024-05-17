package com.santeut.party.controller;

import com.santeut.party.common.response.BasicResponse;
import com.santeut.party.common.util.ResponseUtil;
import com.santeut.party.dto.request.CreatePartyRequestDto;
import com.santeut.party.dto.request.ModifyPartyRequestDto;
import com.santeut.party.service.PartyService;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PartyController {

  private final PartyService partyService;

  @PostMapping("/")
  public ResponseEntity<BasicResponse> createParty(HttpServletRequest request,
      @RequestBody CreatePartyRequestDto requestDto) {
    partyService.createParty(Integer.parseInt(request.getHeader("userId")), requestDto);
    return ResponseUtil.buildBasicResponse(HttpStatus.CREATED, "소모임 생성 성공");
  }

  @PatchMapping("/{partyId}")
  public ResponseEntity<BasicResponse> modifyParty(HttpServletRequest request,
      @PathVariable("partyId") int partyId, @RequestBody ModifyPartyRequestDto requestDto) {
    return ResponseUtil.buildBasicResponse(HttpStatus.OK,
        partyService.modifyParty(Integer.parseInt(request.getHeader("userId")), partyId, requestDto));
  }

  @DeleteMapping("/{partyId}")
  public ResponseEntity<BasicResponse> deleteParty(HttpServletRequest request,
      @PathVariable("partyId") int partyId) {
    partyService.deleteParty(Integer.parseInt(request.getHeader("userId")), partyId);
    return ResponseUtil.buildBasicResponse(HttpStatus.OK,"소모임 삭제 성공");
  }

  @GetMapping("")
  public ResponseEntity<BasicResponse> findParty(
      HttpServletRequest request,
      @RequestParam(name = "guild", required = false) Integer guildId,
      @RequestParam(name = "name", required = false) String name,
      @RequestParam(name = "start", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
      @RequestParam(name = "end", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
  ) {

    return ResponseUtil.buildBasicResponse(HttpStatus.OK,
        partyService.findParty(Integer.parseInt(request.getHeader("userId")), guildId, name,
            startDate,
            endDate));
  }

  @GetMapping("/{partyId}")
  public ResponseEntity<BasicResponse> findPartyById(
      HttpServletRequest request,
      @PathVariable("partyId") int partyId
  ) {
    return ResponseUtil.buildBasicResponse(HttpStatus.OK,
        partyService.findPartyById(Integer.parseInt(request.getHeader("userId")), partyId));
  }

  @GetMapping("/{partyId}/userId")
  ResponseEntity<BasicResponse> getPartyUserId(@PathVariable("partyId") int partyId){
    return ResponseUtil.buildBasicResponse(HttpStatus.OK,
            partyService.findUserIdById(partyId));
  }


}
