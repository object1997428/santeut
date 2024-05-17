package com.santeut.party.service;

import com.santeut.party.dto.request.CreatePartyRequestDto;
import com.santeut.party.dto.request.ModifyPartyRequestDto;
import com.santeut.party.dto.response.GetPartyUserIdResponse;
import com.santeut.party.dto.response.PartyInfoResponseDto;
import com.santeut.party.dto.response.PartyInfoResponseDto.PartyInfo;
import java.time.LocalDate;

public interface PartyService {

  void createParty(int userId, CreatePartyRequestDto requestDto);

  PartyInfo modifyParty(int userId, int partyId, ModifyPartyRequestDto requestDto);

  void deleteParty(int userId, int partyId);

  PartyInfoResponseDto findParty(int userId, Integer guildId, String name, LocalDate startDate,
      LocalDate endDate);

  PartyInfo findPartyById(int userId, int partyId);

  GetPartyUserIdResponse findUserIdById(int partyId);
}
