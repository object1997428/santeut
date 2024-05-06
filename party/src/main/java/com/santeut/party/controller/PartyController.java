package com.santeut.party.controller;

import com.santeut.party.common.response.BasicResponse;
import com.santeut.party.common.response.PagingResponse;
import com.santeut.party.common.util.ResponseUtil;
import com.santeut.party.dto.request.CreatePartyRequestDto;
import com.santeut.party.dto.request.ModifyPartyRequestDto;
import com.santeut.party.dto.response.PartyInfoResponseDto;
import com.santeut.party.service.PartyService;
import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PartyController {

  private final PartyService partyService;

  @PostMapping("/")
  public ResponseEntity<BasicResponse> createParty(@RequestHeader("userId") String userId,
      @RequestBody CreatePartyRequestDto requestDto) {
    partyService.createParty(Integer.parseInt(userId), requestDto);
    return ResponseUtil.buildBasicResponse(HttpStatus.CREATED, "소모임 생성 성공");
  }

  @PatchMapping("/{partyId}")
  public ResponseEntity<BasicResponse> modifyParty(@RequestHeader("userId") String userId,
      @PathVariable("partyId") int partyId, @RequestBody ModifyPartyRequestDto requestDto) {
    return ResponseUtil.buildBasicResponse(HttpStatus.OK,
        partyService.modifyParty(Integer.parseInt(userId), partyId, requestDto));
  }

  @DeleteMapping("/{partyId}")
  public ResponseEntity<BasicResponse> deleteParty(@RequestHeader("userId") String userId,
      @PathVariable("partyId") int partyId) {
    partyService.deleteParty(Integer.parseInt(userId), partyId);
    return ResponseUtil.buildBasicResponse(HttpStatus.OK,"소모임 삭제 성공");
  }

  @GetMapping("")
  public ResponseEntity<PagingResponse> findParty(
      @RequestHeader("userId") String userId,
      @RequestParam(name = "guild", required = false) Integer guildId,
      @RequestParam(name = "name", required = false) String name,
      @RequestParam(name = "start", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
      @RequestParam(name = "end", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
      @PageableDefault(page = 0, size = 5, sort = "schedule", direction = Sort.Direction.ASC) Pageable pageable) {

    Page<PartyInfoResponseDto> party = partyService.findParty(Integer.parseInt(userId), guildId, name, startDate,
        endDate, pageable);
    return ResponseUtil.buildPagingResponse(HttpStatus.OK, party.getContent(), party.isFirst(),
        party.isLast(), party.getNumber(), party.getTotalPages(), party.getTotalElements(),
        party.getSize(), true, true, true);

  }


}
