package com.santeut.guild.repository;

import com.santeut.guild.entity.GuildUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuildUserRepository extends JpaRepository<GuildUserEntity, Integer> {
}
