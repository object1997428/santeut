package com.santeut.party.repository;

import com.santeut.party.dto.response.PartyByYearMonthResponse;
import com.santeut.party.entity.PartyUser;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface PartyUserRepository extends JpaRepository<PartyUser, Integer> {

    boolean existsByUserIdAndPartyId(int userId, int partyId);

    Optional<PartyUser> findByPartyIdAndUserId(int partyId, int userId);

    List<PartyUser> findAllByPartyId(int partyId);

    @Query("SELECT p.userId FROM PartyUser p WHERE p.partyId = :partyId AND p.status = :status AND p.isDeleted = false")
    List<Integer> findUserIdsByPartyIdAndStatus(@Param("partyId") Integer partyId, @Param("status") char status);

    @Query("SELECT p.userId FROM PartyUser p WHERE p.partyId = :partyId")
    List<Integer> findUserIdsByPartyId(@Param("partyId") Integer partyId);

    boolean existsByUserIdAndMountainIdAndStatus(int userId, int mountainId, char status);

    @Query("SELECT p.schedule FROM PartyUser pu JOIN Party p on pu.partyId = p.partyId WHERE p.status != 'I' and pu.userId = :userId and Year(p.schedule) = :year and Month(p.schedule) = :month")
    List<LocalDateTime> findMyPartyByYearAndMonth(@Param("userId") int userId, @Param("year")
    int year, @Param("month") int month);

}
