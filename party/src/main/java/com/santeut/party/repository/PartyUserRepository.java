package com.santeut.party.repository;

import com.santeut.party.entity.PartyUser;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyUserRepository extends JpaRepository<PartyUser, Integer> {

  boolean existsByUserIdAndPartyId(int userId, int partyId);

  List<PartyUser> findAllByPartyId(int partyId);
}
