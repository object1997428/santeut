package com.santeut.guild.repository;

import com.santeut.guild.entity.GuildEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuildRepository extends JpaRepository<GuildEntity, Integer> {

    boolean existsByGuildName(String guildName);

    Optional<GuildEntity> findByGuildId(int guildId);

    @Query("SELECT g FROM GuildEntity g WHERE g.guildIsPrivate = false AND g.isDeleted = false")
    List<GuildEntity> findByAllGuild();
}
