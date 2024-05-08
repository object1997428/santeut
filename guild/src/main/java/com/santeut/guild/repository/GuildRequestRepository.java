package com.santeut.guild.repository;

import com.santeut.guild.entity.GuildRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuildRequestRepository extends JpaRepository<GuildRequestEntity, Integer> {

    GuildRequestEntity findByGuildIdAndUserId(int guildId, int userId);
    List<GuildRequestEntity> findByGuildId(int guildId);
}
