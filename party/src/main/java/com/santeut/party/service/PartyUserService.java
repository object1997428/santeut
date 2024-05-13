package com.santeut.party.service;

import com.santeut.party.dto.response.HikingRecordResponse;
import com.santeut.party.dto.response.HikingStartResponse;
import com.santeut.party.dto.response.PartyByYearMonthResponse;
import com.santeut.party.dto.response.PartyInfoResponseDto;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PartyUserService {

  void joinUserParty(int userId, int partyId);

  void deleteAllPartyUser(int partyId, char status);

  void withdrawUserFromParty(int userId, Integer partyId);

  Page<PartyInfoResponseDto> findMyParty(int userId, boolean includeEnd, LocalDate date,
      Pageable pageable);

  HikingStartResponse findMyHikingTrailByPartyUserId(int partyUserId);

  PartyByYearMonthResponse findMyPartyByYearMonth(int userId, int year, int month);

  List<HikingRecordResponse> findMyEndedHikingRecord(int userId);
}
