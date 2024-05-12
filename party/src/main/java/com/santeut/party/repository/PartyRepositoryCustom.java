package com.santeut.party.repository;

import com.santeut.party.dto.response.PartyWithPartyUserIdResponse;
import com.santeut.party.entity.Party;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface PartyRepositoryCustom {

  Page<Party> findPartyWithSearchConditions(int userId, Integer guildId, String name,
      LocalDate startDate, LocalDate endDate, Pageable pageable);

  Page<PartyWithPartyUserIdResponse> findMyPartyWithSearchCondition(boolean includeEnd,
      LocalDate date, int userId, Pageable pageable);
}
