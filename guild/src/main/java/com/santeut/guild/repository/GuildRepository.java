package com.santeut.guild.repository;

import com.santeut.guild.entity.GuildEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuildRepository extends JpaRepository<GuildEntity, Integer> {

    boolean existsByGuildName(String guildName);
    Optional<GuildEntity> findByGuildName(String guildName);
    Optional<GuildEntity> findByGuildId(int guildId);
    @Query("SELECT g FROM GuildEntity g WHERE g.guildIsPrivate = false AND g.isDeleted = false")
    List<GuildEntity> findByAllGuild();
    @Query("SELECT g FROM GuildEntity g " +
            "JOIN GuildUserEntity gu ON g.guildId = gu.guildId " +
            "WHERE gu.userId = :userId AND gu.isDeleted = false")
    List<GuildEntity> findByMyGuild(@Param("userId") int userId);

    @Query("SELECT g FROM GuildEntity g ORDER BY g.guildId DESC")
    Optional<GuildEntity> findLastRecord();

    @Query("SELECT g FROM GuildEntity g WHERE " +
    "(:regionId = 0 OR g.regionId = :regionId) AND " +
    "(:guildGender = 'A' OR g.guildGender = :guildGender) AND "+
    "g.isDeleted = false AND g.guildIsPrivate = false")
    List<GuildEntity> searchGuild(@Param("regionId") Integer regionId, @Param("guildGender") String guildGender);

    @Query("SELECT g FROM GuildEntity g WHERE g.regionId = :regionId AND g.isDeleted = false AND g.guildIsPrivate = false")
    List<GuildEntity> searchRegionName(@Param("regionId") int regionId);
    @Query("SELECT g FROM GuildEntity g WHERE g.guildGender = :guildGender AND g.isDeleted = false AND g.guildIsPrivate = false")
    List<GuildEntity> searchGender(@Param("guildGender") String guildGender);
    @Query("SELECT g FROM GuildEntity g WHERE g.regionId = :regionId AND g.guildGender = :guildGender AND g.isDeleted = false AND g.guildIsPrivate = false ")
    List<GuildEntity> searchRegionNameAndGender(@Param("regionId") int regionId, @Param("guildGender") String guildGender);
}
