package com.santeut.party.service;

import com.santeut.party.common.exception.AccessDeniedException;
import com.santeut.party.common.exception.AlreadyJoinedException;
import com.santeut.party.common.exception.DataNotFoundException;
import com.santeut.party.entity.Party;
import com.santeut.party.entity.PartyUser;
import com.santeut.party.repository.PartyRepository;
import com.santeut.party.repository.PartyUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PartyUserServiceImpl implements PartyUserService {

  private final PartyRepository partyRepository;
  private final PartyUserRepository partyUserRepository;

  @Override
  @Transactional
  public void joinUserParty(int userId, int partyId) {
    Party party = partyRepository.findById(partyId)
        .orElseThrow(() -> new DataNotFoundException("해당 소모임이 존재하지 않습니다"));
    if(partyUserRepository.existsByUserIdAndPartyId(userId, partyId)) {
      throw new AlreadyJoinedException("이미 가입한 소모임입니다");
    }
    if(party.addParticipant()) {
      partyUserRepository.save(PartyUser.of(userId, partyId));
    } else {
      throw new AccessDeniedException("소모임 인원이 이미 모두 찼습니다");
    }
  }

}
