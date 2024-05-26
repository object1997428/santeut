package com.santeut.party.repository;

import com.santeut.party.dto.response.PartyWithPartyUserIdResponse;
import com.santeut.party.entity.Party;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface PartyRepositoryCustom {

  List<Party> findPartyWithSearchConditions(int userId, Integer guildId, String name,
      LocalDate startDate, LocalDate endDate);

  List<PartyWithPartyUserIdResponse> findMyPartyWithSearchCondition(boolean includeEnd,
      LocalDate date, int userId);
}
