package com.santeut.party.service;

import com.santeut.party.common.exception.AccessDeniedException;
import com.santeut.party.common.exception.AlreadyJoinedException;
import com.santeut.party.common.exception.DataNotFoundException;
import com.santeut.party.common.util.GeometryUtils;
import com.santeut.party.dto.request.HikingRecordRequestInterface;
import com.santeut.party.dto.response.HikingRecordResponse;
import com.santeut.party.dto.response.HikingStartResponse;
import com.santeut.party.dto.response.PartyByYearMonthResponse;
import com.santeut.party.dto.response.PartyInfoResponseDto;
import com.santeut.party.dto.response.PartyWithPartyUserIdResponse;
import com.santeut.party.entity.Party;
import com.santeut.party.entity.PartyUser;
import com.santeut.party.feign.GuildAccessUtil;
import com.santeut.party.feign.UserInfoAccessUtil;
import com.santeut.party.repository.PartyRepository;
import com.santeut.party.repository.PartyUserRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PartyUserServiceImpl implements PartyUserService {

  private final PartyRepository partyRepository;
  private final PartyUserRepository partyUserRepository;
  private final UserInfoAccessUtil userInfoAccessUtil;
  private final GuildAccessUtil guildAccessUtil;

  @Override
  @Transactional
  public void joinUserParty(int userId, int partyId) {
    log.info("소모임 가입 요청: userId, partyId = "+userId+", "+partyId);
    Party party = partyRepository.findById(partyId)
        .orElseThrow(() -> new DataNotFoundException("해당 소모임이 존재하지 않습니다"));
    if(partyUserRepository.existsByUserIdAndPartyId(userId, partyId)) {
      throw new AlreadyJoinedException("이미 가입한 소모임입니다");
    }
    if(party.addParticipant()) {
      partyUserRepository.save(PartyUser.of(userId, partyId, party.getMountainId()));
    } else {
      throw new AccessDeniedException("소모임 인원이 이미 모두 찼습니다");
    }
  }

  @Override
  @Transactional
  public void deleteAllPartyUser(int partyId, char status) {
    partyUserRepository.findAllByPartyId(partyId)
        .forEach(partyUser -> partyUser.setStatus(status));
  }

  @Override
  @Transactional
  public void withdrawUserFromParty(int userId, Integer partyId) {
    log.info("소모임 나가기: userId, partyId = "+userId+", "+partyId);
    Party party = partyRepository.findById(partyId)
        .orElseThrow(() -> new DataNotFoundException("해당 소모임이 존재하지 않습니다"));
    PartyUser partyUser = partyUserRepository.findByPartyIdAndUserId(party.getPartyId(), userId)
        .orElseThrow(() -> new AccessDeniedException("해당 소모임에 가입하지 않았습니다"));
    if (party.getUserId() == userId) {
      // 소모임장이 탈퇴할 경우 다른 회원들도 자동으로 소모임에서 나감
      party.deleteParty('I');
      deleteAllPartyUser(partyId, 'I');
    } else {
      // 소모임 나감
      partyUserRepository.delete(partyUser);
      party.minusParticipant();
    }
  }

  @Override
  public Page<PartyInfoResponseDto> findMyParty(int userId, boolean includeEnd, LocalDate date,
      Pageable pageable) {
    Page<PartyWithPartyUserIdResponse> myParties = partyRepository.findMyPartyWithSearchCondition(
        includeEnd, date, userId, pageable);
    return myParties.map(p -> {
      String owner = userInfoAccessUtil.getUserInfo(p.getParty().getUserId()).getUserNickname();
      String guildName = (p.getParty().getGuildId() == null) ? ""
          : guildAccessUtil.getGuildInfo(p.getParty().getGuildId()).getGuildName();
      return PartyInfoResponseDto.of(owner, p.getPartyUserId(), p.getParty(), null, guildName);
    });
  }

  @Override
  public HikingStartResponse findMyHikingTrailByPartyUserId(int partyUserId) {
    log.info("partyUserId: "+partyUserId+"/등산 경로 조회");
    PartyUser partyUser = partyUserRepository.findById(partyUserId)
        .orElseThrow(() -> new DataNotFoundException("사용자-소모임이 존재하지 않습니다"));
    return new HikingStartResponse(GeometryUtils.convertGeometryToListLocationData(
        partyUser.getPoints()));
  }

  @Override
  public PartyByYearMonthResponse findMyPartyByYearMonth(int userId, int year, int month) {
    List<LocalDateTime> dateList = partyUserRepository.findMyPartyByYearAndMonth(userId, year, month);
    Set<String> dateSet = new HashSet<>();
    for(LocalDateTime localDateTime:dateList) {
      dateSet.add(localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }
    return new PartyByYearMonthResponse(dateSet);
  }

  @Override
  public Page<HikingRecordResponse> findMyEndedHikingRecord(int userId, Pageable pageable) {
    Page<HikingRecordRequestInterface> hikingRecord = partyUserRepository.findMyHikingRecord(userId,
        pageable);

    return hikingRecord.map(r -> {
      String guildName = (r.getGuildId() == null) ? ""
          : guildAccessUtil.getGuildInfo(r.getGuildId()).getGuildName();
      return HikingRecordResponse.of(r.getPartyUserId(), r.getPartyName(), guildName,
          r.getMountainName(), r.getSchedule(), r.getDistance(), r.getBestHeight(),
          r.getMoveTime());

    });
  }
}
