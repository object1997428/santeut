package com.santeut.party.repository;


import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.DateTimeTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.santeut.party.dto.response.PartyWithPartyUserIdResponse;
import com.santeut.party.dto.response.QPartyWithPartyUserIdResponse;
import com.santeut.party.entity.Party;
import com.santeut.party.entity.QPartyUser;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.santeut.party.entity.QParty;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PartyRepositoryImpl implements PartyRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;
  QParty party;
  QPartyUser partyUser;

  @Override
  public Page<Party> findPartyWithSearchConditions(int userId, Integer guildId, String name,
      LocalDate startDate,
      LocalDate endDate, Pageable pageable) {
    party = QParty.party;

    List<Party> content = jpaQueryFactory
        .selectFrom(party)
        .where(dateBetween(startDate, endDate), guildIdEq(guildId), partyNameContains(name),
            party.status.eq('B'))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    JPAQuery<Long> count = jpaQueryFactory
        .select(party.count())
        .from(party)
        .where(dateBetween(startDate, endDate), guildIdEq(guildId), partyNameContains(name),
            party.status.eq('B'));

    return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
  }

  @Override
  public Page<PartyWithPartyUserIdResponse> findMyPartyWithSearchCondition(boolean includeEnd, LocalDate date, int userId,
      Pageable pageable) {
    party = QParty.party;
    partyUser = QPartyUser.partyUser;

    List<PartyWithPartyUserIdResponse> content = jpaQueryFactory
        .select(new QPartyWithPartyUserIdResponse(party, partyUser.partyUserId))
        .from(party)
        .join(partyUser)
        .on(partyUser.partyId.eq(party.partyId))
        .where(partyUser.userId.eq(userId), dateBetween(date, date), partyStatus(includeEnd))
        .fetch();

    JPAQuery<Long> count = jpaQueryFactory
        .select(party.count())
        .from(party)
        .join(partyUser)
        .on(partyUser.partyId.eq(party.partyId)
            .and(partyUser.userId.eq(userId)))
        .where(dateBetween(date, date), partyStatus(includeEnd));

    return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
  }

  private BooleanExpression partyStatus(boolean includeEnd) {
    if(includeEnd) {
      return party.status.in(List.of('B', 'P','E'));
    } else {
      return party.status.in(List.of('B', 'P'));
    }
  }

  private BooleanExpression dateBetween(LocalDate dateFrom, LocalDate dateTo) {

    if (dateFrom == null && dateTo == null) {
      return null;
    }

    BooleanExpression isGoeStartDate = party.schedule.goe(LocalDateTime.of(dateFrom, LocalTime.MIN));
    BooleanExpression isLoeEndDate = party.schedule.loe(LocalDateTime.of(dateTo, LocalTime.MAX).withNano(0));

    return Expressions.allOf(isGoeStartDate, isLoeEndDate);
  }

  private BooleanExpression guildIdEq(Integer guildId) {
    if (guildId == null) {
      return null;
    }
    return party.guildId.eq(guildId);
  }

  private BooleanExpression partyNameContains(String partyName) {
    if (partyName == null) {
      return null;
    }
    return party.partyName.contains(partyName);
  }

}
