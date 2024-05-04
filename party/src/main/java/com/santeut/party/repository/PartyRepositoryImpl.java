package com.santeut.party.repository;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.santeut.party.entity.Party;
import com.santeut.party.entity.QParty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
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

  @Override
  public Page<Party> findPartyWithSearchConditions(int userId, Integer guildId, String name,
      LocalDate startDate,
      LocalDate endDate, Pageable pageable) {
    party = QParty.party;

    List<Party> content = jpaQueryFactory
        .selectFrom(party)
        .where(dateBetween(startDate, endDate), guildIdEq(guildId), partyNameContains(name),
            partyStatusEq('B'))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    JPAQuery<Long> count = jpaQueryFactory
        .select(party.count())
        .from(party)
        .where(dateBetween(startDate, endDate), guildIdEq(guildId), partyNameContains(name),
            partyStatusEq('B'));

    return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
  }

  private BooleanExpression dateBetween(LocalDate dateFrom, LocalDate dateTo) {

    if (dateFrom == null & dateTo == null) {
      return null;
    }

    return party.schedule.between(LocalDateTime.of(dateFrom, LocalTime.of(0, 0)),
        LocalDateTime.of(dateTo, LocalTime.of(23, 59)));
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

  private BooleanExpression partyStatusEq(char status) {
    return party.status.eq(status);
  }

}
