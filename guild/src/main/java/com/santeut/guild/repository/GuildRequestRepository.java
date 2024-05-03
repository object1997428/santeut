package com.santeut.guild.repository;

import com.santeut.guild.entity.GuildRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuildRequestRepository extends JpaRepository<GuildRequestEntity, Integer> {
}
