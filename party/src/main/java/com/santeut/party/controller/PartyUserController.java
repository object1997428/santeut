package com.santeut.party.controller;

import com.santeut.party.common.response.BasicResponse;
import com.santeut.party.common.response.PagingResponse;
import com.santeut.party.common.util.ResponseUtil;
import com.santeut.party.dto.response.HikingRecordResponse;
import com.santeut.party.dto.response.PartyInfoResponseDto;
import com.santeut.party.service.PartyUserService;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PartyUserController {

  private final PartyUserService partyUserService;

  @PostMapping("/user/join")
  public ResponseEntity<BasicResponse> joinParty(HttpServletRequest request,
      @RequestBody Map<String, Integer> map) {
    partyUserService.joinUserParty(Integer.parseInt(request.getHeader("userId")), map.get("partyId"));
    return ResponseUtil.buildBasicResponse(HttpStatus.OK, "소모임 가입 완료");
  }

  @DeleteMapping("/user/withdraw")
  public ResponseEntity<BasicResponse> withdrawParty(HttpServletRequest request,
      @RequestBody Map<String, Integer> map) {
    partyUserService.withdrawUserFromParty(Integer.parseInt(request.getHeader("userId")), map.get("partyId"));
    return ResponseUtil.buildBasicResponse(HttpStatus.OK, "소모임 탈퇴 성공");
  }


  @GetMapping("/user")
  public ResponseEntity<PagingResponse> findMyParty(
      HttpServletRequest request,
      @RequestParam(name = "includeEnd") boolean includeEnd,
      @RequestParam(name = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
      @PageableDefault(page = 0, size = 10, sort = "schedule", direction = Sort.Direction.ASC) Pageable pageable) {
    Page<PartyInfoResponseDto> myParty = partyUserService.findMyParty(
        Integer.parseInt(request.getHeader("userId")), includeEnd, date, pageable);
    return ResponseUtil.buildPagingResponse(HttpStatus.OK, myParty.getContent(), myParty.isFirst(),
        myParty.isLast(), myParty.getNumber(), myParty.getTotalPages(), myParty.getTotalElements(),
        myParty.getSize(), true, true, true);

  }

  @GetMapping("/user/{partyUserId}")
  public ResponseEntity<BasicResponse> findMyHikingTrailByPartyUserId(
      HttpServletRequest request,
      @PathVariable("partyUserId") int partyUserId
  ) {
    return ResponseUtil.buildBasicResponse(HttpStatus.OK,
        partyUserService.findMyHikingTrailByPartyUserId(partyUserId));
  }

  @GetMapping("/user/schedule")
  public ResponseEntity<BasicResponse> findMyPartyByYearMonth(
      HttpServletRequest request,
      @RequestParam("year") int year,
      @RequestParam("month") int month
  ) {
    return ResponseUtil.buildBasicResponse(HttpStatus.OK,
        partyUserService.findMyPartyByYearMonth(Integer.parseInt(request.getHeader("userId")), year,
            month));
  }

  @GetMapping("/user/record")
  public ResponseEntity getMyHikingRecord(
      HttpServletRequest request,
      @PageableDefault(page = 0, size = 10, sort = "mountain_id", direction = Sort.Direction.DESC) Pageable pageable
  ) {
    Page<HikingRecordResponse> hikingRecord = partyUserService.findMyEndedHikingRecord(
        Integer.parseInt(request.getHeader("userId")), pageable);
    return ResponseUtil.buildPagingResponse(HttpStatus.OK, hikingRecord.getContent(),
        hikingRecord.isFirst(),
        hikingRecord.isLast(), hikingRecord.getNumber(), hikingRecord.getTotalPages(),
        hikingRecord.getTotalElements(), hikingRecord.getSize(), true, false, false);
  }

}
