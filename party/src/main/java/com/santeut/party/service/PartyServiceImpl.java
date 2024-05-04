package com.santeut.party.service;

import com.santeut.party.common.exception.AccessDeniedException;
import com.santeut.party.common.exception.DataNotFoundException;
import com.santeut.party.dto.request.CreatePartyRequestDto;
import com.santeut.party.dto.request.ModifyPartyRequestDto;
import com.santeut.party.dto.response.PartyInfoResponseDto;
import com.santeut.party.entity.Party;
import com.santeut.party.feign.UserInfoAccessUtil;
import com.santeut.party.repository.PartyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PartyServiceImpl implements PartyService {

  private final PartyRepository partyRepository;
  private final PartyUserService partyUserService;
  private final UserInfoAccessUtil userInfoAccessUtil;

  @Override
  @Transactional
  public void createParty(int userId, CreatePartyRequestDto requestDto) {
    log.info("소모임 생성 정보: " + requestDto);
    Party entity = partyRepository.save(Party.createEntity(userId, requestDto));
    partyUserService.joinUserParty(userId, entity.getPartyId());
  }

  @Override
  @Transactional
  public PartyInfoResponseDto modifyParty(int userId, int partyId,
      ModifyPartyRequestDto requestDto) {
    log.info("소모임 수정" + requestDto.getPlace() + ", " + requestDto.getSchedule() + ", "
        + requestDto.getPartyName());
    Party entity = partyRepository.findById(partyId)
        .orElseThrow(() -> new DataNotFoundException("[party] 해당 소모임이 존재하지 않습니다"));
    if (entity.getUserId() != userId) {
      log.error("소모임 수정 권한이 없음");
      throw new AccessDeniedException("[party] 소모임 수정 권한이 없습니다");
    }
    entity.modifyPartyInfo(requestDto.getPartyName(), requestDto.getSchedule(),
        requestDto.getPlace(), requestDto.getMaxPeople());
    return PartyInfoResponseDto.of(
        userInfoAccessUtil.getUserInfo(entity.getUserId()).getUserNickname()
        , entity);
  }
}
