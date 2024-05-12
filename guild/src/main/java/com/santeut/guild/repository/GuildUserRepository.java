package com.santeut.guild.repository;

import com.santeut.guild.entity.GuildUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuildUserRepository extends JpaRepository<GuildUserEntity, Integer> {

    List<GuildUserEntity> findByUserId(int userId);
    Optional<GuildUserEntity> findByGuildIdAndUserId(int guildId, int userId);

    Optional<GuildUserEntity> findByGuildId(int guildId);

    @Query("SELECT gu FROM GuildUserEntity gu WHERE gu.guildId = :guildId AND gu.isDeleted = false")
    List<GuildUserEntity> findByGuildUserList(int guildId);
}
