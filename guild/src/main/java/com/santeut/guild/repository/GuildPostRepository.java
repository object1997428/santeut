package com.santeut.guild.repository;

import com.santeut.guild.entity.GuildEntity;
import com.santeut.guild.entity.GuildPostEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuildPostRepository extends JpaRepository<GuildPostEntity, Integer> {
    Optional<List<GuildPostEntity>> findAllByGuildIdAndCategoryIdAndIdLessThanOrderByIdDesc(int guildId, int categoryId, int id, Pageable pageable);
}
