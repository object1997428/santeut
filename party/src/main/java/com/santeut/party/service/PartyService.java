package com.santeut.party.service;

import com.santeut.party.dto.request.CreatePartyRequestDto;
import com.santeut.party.dto.request.ModifyPartyRequestDto;
import com.santeut.party.dto.response.PartyInfoResponseDto;

public interface PartyService {

  void createParty(int userId, CreatePartyRequestDto requestDto);

  PartyInfoResponseDto modifyParty(int userId, int partyId, ModifyPartyRequestDto requestDto);
}
