package com.santeut.party.service;

import com.santeut.party.dto.request.CreatePartyRequestDto;
import com.santeut.party.entity.Party;
import com.santeut.party.repository.PartyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PartyServiceImpl implements PartyService {

  private final PartyRepository partyRepository;

  @Override
  public void createParty(int userId, CreatePartyRequestDto requestDto) {
    log.info("소모임 생성 정보: "+requestDto);
    partyRepository.save(Party.createEntity(userId, requestDto));
  }
}
