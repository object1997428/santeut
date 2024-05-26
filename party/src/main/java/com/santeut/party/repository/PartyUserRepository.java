package com.santeut.party.repository;

import com.santeut.party.dto.request.HikingRecordRequestInterface;
import com.santeut.party.entity.Party;
import com.santeut.party.entity.PartyUser;
import java.time.LocalDateTime;
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

    @Query(nativeQuery = true,
        value="SELECT pu.party_user_id as partyUserId, p.party_name as partyName, p.guild_id as guildId, p.party_mountain_name as mountainName, p.party_schedule as schedule, pu.party_user_distance as distance, pu.party_user_best_height as bestHeight, pu.party_user_move_time as moveTime FROM party_user pu JOIN party p on pu.user_id=:userId and pu.party_id = p.party_id WHERE pu.party_user_status like 'E' ORDER BY pu.party_user_started_at DESC")
    List<HikingRecordRequestInterface> findMyHikingRecord(@Param("userId") int userId);

    @Query("SELECT p FROM PartyUser pu JOIN Party p ON pu.partyId = p.partyId AND pu.userId = :userId WHERE (p.status like 'B' OR p.status like 'P') AND p.isDeleted = false")
    List<Party> findMyPartyForChat(@Param("userId") int userId);

    @Query("SELECT pu.userId FROM PartyUser pu WHERE pu.partyId = :partyId AND pu.isDeleted = false")
    List<Integer> findAllMemberByPartyId(@Param("partyId") int partyId);

}
