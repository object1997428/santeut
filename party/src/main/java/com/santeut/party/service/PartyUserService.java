package com.santeut.party.service;

import com.santeut.party.dto.response.HikingRecordResponse;
import com.santeut.party.dto.response.PartyByYearMonthResponse;
import com.santeut.party.dto.response.PartyInfoResponseDto;
import java.time.LocalDate;

import com.santeut.party.feign.dto.response.HikingTrackResponse;

public interface PartyUserService {

  void joinUserParty(int userId, int partyId);

  void deleteAllPartyUser(int partyId, char status);

  void withdrawUserFromParty(int userId, Integer partyId);

  PartyInfoResponseDto findMyParty(int userId, boolean includeEnd, LocalDate date);

  HikingTrackResponse findMyHikingTrailByPartyUserId(int partyUserId);

  PartyByYearMonthResponse findMyPartyByYearMonth(int userId, int year, int month);

  HikingRecordResponse findMyEndedHikingRecord(int userId);
}
