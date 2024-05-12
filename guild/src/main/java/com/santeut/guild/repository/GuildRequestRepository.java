package com.santeut.guild.repository;

import com.santeut.guild.entity.GuildRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuildRequestRepository extends JpaRepository<GuildRequestEntity, Integer> {

    Optional<GuildRequestEntity> findByGuildIdAndUserId(int guildId, int userId);

    @Query("SELECT gr FROM GuildRequestEntity gr WHERE gr.status = 'R'")
    List<GuildRequestEntity> findByGuildId(int guildId);
}
