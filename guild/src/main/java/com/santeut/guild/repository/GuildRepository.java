package com.santeut.guild.repository;

import com.santeut.guild.entity.GuildEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuildRepository extends JpaRepository<GuildEntity, Integer> {


}
