package com.santeut.party.repository;

import com.santeut.party.entity.Party;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyRepository extends JpaRepository<Party, Integer>, PartyRepositoryCustom {

}
