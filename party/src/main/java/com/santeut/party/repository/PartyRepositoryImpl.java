package com.santeut.party.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.santeut.party.dto.response.PartyWithPartyUserIdResponse;
import com.santeut.party.dto.response.QPartyWithPartyUserIdResponse;
import com.santeut.party.entity.Party;
import com.santeut.party.entity.QPartyUser;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import com.santeut.party.entity.QParty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PartyRepositoryImpl implements PartyRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;
  QParty party;
  QPartyUser partyUser;

  @Override
  public List<Party> findPartyWithSearchConditions(int userId, Integer guildId, String name,
      LocalDate startDate,
      LocalDate endDate) {
    party = QParty.party;

    return jpaQueryFactory
        .selectFrom(party)
        .where(dateBetween(startDate, endDate), guildIdEq(guildId), partyNameContains(name),
            party.status.eq('B'))
        .fetch();
  }

  @Override
  public List<PartyWithPartyUserIdResponse> findMyPartyWithSearchCondition(boolean includeEnd,
      LocalDate date, int userId
  ) {
    party = QParty.party;
    partyUser = QPartyUser.partyUser;

    return jpaQueryFactory
        .select(new QPartyWithPartyUserIdResponse(party, partyUser.partyUserId))
        .from(party)
        .join(partyUser)
        .on(partyUser.partyId.eq(party.partyId))
        .where(partyUser.userId.eq(userId), dateBetween(date, date), partyStatus(includeEnd))
        .fetch();
  }

  private BooleanExpression partyStatus(boolean includeEnd) {
    if (includeEnd) {
      return party.status.in(List.of('B', 'P', 'E'));
    } else {
      return party.status.in(List.of('B', 'P'));
    }
  }

  private BooleanExpression dateBetween(LocalDate dateFrom, LocalDate dateTo) {

    if (dateFrom == null && dateTo == null) {
      return null;
    }

    BooleanExpression isGoeStartDate = party.schedule.goe(
        LocalDateTime.of(dateFrom, LocalTime.MIN));
    BooleanExpression isLoeEndDate = party.schedule.loe(
        LocalDateTime.of(dateTo, LocalTime.MAX).withNano(0));

    return Expressions.allOf(isGoeStartDate, isLoeEndDate);
  }

  private BooleanExpression guildIdEq(Integer guildId) {
    if (guildId == null) {
      // 어느 길드에도 소속되지 않은 소모임 조회
      return party.guildId.isNull();
    }
    // 특정 길드에 소속된 소모임만 조회
    return party.guildId.eq(guildId);
  }

  private BooleanExpression partyNameContains(String partyName) {
    if (partyName == null) {
      return null;
    }
    return party.partyName.contains(partyName);
  }

}
