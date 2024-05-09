package com.santeut.party.service;

import com.santeut.party.dto.request.CreatePartyRequestDto;
import com.santeut.party.dto.request.ModifyPartyRequestDto;
import com.santeut.party.dto.response.PartyInfoResponseDto;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PartyService {

  void createParty(int userId, CreatePartyRequestDto requestDto);

  PartyInfoResponseDto modifyParty(int userId, int partyId, ModifyPartyRequestDto requestDto);

  void deleteParty(int userId, int partyId);

  Page<PartyInfoResponseDto> findParty(int userId, Integer guildId, String name, LocalDate startDate,
      LocalDate endDate, Pageable pageable);

  PartyInfoResponseDto findPartyById(int userId, int partyId);

}
