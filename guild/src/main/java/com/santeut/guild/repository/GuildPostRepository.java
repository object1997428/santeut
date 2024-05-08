package com.santeut.guild.repository;

import com.santeut.guild.entity.GuildEntity;
import com.santeut.guild.entity.GuildPostEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface GuildPostRepository extends JpaRepository<GuildPostEntity, Integer> {
    Optional<List<GuildPostEntity>> findAllByGuildIdAndCategoryIdAndIsDeletedFalseAndIdLessThanOrderByIdDesc(int guildId, int categoryId, int id, Pageable pageable);

    // 게시글 삭제
    @Modifying
    @Transactional
    @Query("UPDATE GuildPostEntity gp SET gp.isDeleted = true, gp.deletedAt=:deletedAt WHERE gp.id=:guildPostId")
    void deleteGuildPostDirectly(int guildPostId, LocalDateTime deletedAt);

    // 게시글 수정
    @Modifying
    @Transactional
    @Query("UPDATE GuildPostEntity gp SET gp.guildPostTitle = :guildPostTitle, gp.guildPostContent=:guildPostContent, gp.modifiedAt=:modifiedAt WHERE gp.id=:guildPostId")
    void updateGuildPostDirectly(int guildPostId,String guildPostTitle, String guildPostContent, LocalDateTime modifiedAt);

}
