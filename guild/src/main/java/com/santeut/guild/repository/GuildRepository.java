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

    Optional<GuildEntity> findByGuildId(int guildId);
    @Query("SELECT g FROM GuildEntity g WHERE g.guildIsPrivate = false AND g.isDeleted = false ORDER BY g.createdAt DESC")
    List<GuildEntity> findByAllGuild();
    @Query("SELECT g FROM GuildEntity g " +
            "JOIN GuildUserEntity gu ON g.guildId = gu.guildId " +
            "WHERE gu.userId = :userId AND gu.isDeleted = false And g.isDeleted = false ORDER BY g.createdAt DESC")
    List<GuildEntity> findByMyGuild(@Param("userId") int userId);

    @Query("SELECT g FROM GuildEntity g WHERE " +
    "(:regionId = 0 OR g.regionId = :regionId) AND " +
    "(:guildGender = 'A' OR g.guildGender = :guildGender) AND "+
    "g.isDeleted = false AND g.guildIsPrivate = false ORDER BY g.createdAt DESC")
    List<GuildEntity> searchGuild(@Param("regionId") Integer regionId, @Param("guildGender") String guildGender);

    @Query("SELECT g FROM GuildEntity g WHERE g.guildName LIKE %:guildName% AND g.isDeleted = false AND g.guildIsPrivate = false ORDER BY g.createdAt DESC")
    List<GuildEntity> searchGuildName(@Param("guildName") String guildName);

}
