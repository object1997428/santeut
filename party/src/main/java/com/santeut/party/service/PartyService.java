package com.santeut.party.service;

import com.santeut.party.dto.request.CreatePartyRequestDto;

public interface PartyService {

  void createParty(int userId, CreatePartyRequestDto requestDto);

}
