package com.santeut.guild.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "guild_user")
public class GuildUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int guildUserId;

    @NotNull
    private int userId;

    @NotNull
    private int guildId;

    @NotNull
    private LocalDateTime visitedAt;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private LocalDateTime modifiedAt;

    @NotNull
    private boolean isDeleted;

    private LocalDateTime deletedAt;

//    public static GuildUserEntity createGuildUser(GuildEntity guildEntity){
//
//        GuildUserEntity guildUserEntity = new GuildUserEntity();
//        guildUserEntity.userId = guildEntity.getUserId();
//        guildUserEntity.guildId = guildEntity.getGuildId();
//        guildUserEntity.visitedAt = LocalDateTime.now();
//        guildUserEntity.createdAt = LocalDateTime.now();
//        guildUserEntity.modifiedAt = LocalDateTime.now();
//        guildUserEntity.isDeleted = false;
//
//        return  guildUserEntity;
//    }

    public static GuildUserEntity createGuildUser(int guildId, int userId){

        GuildUserEntity guildUserEntity = new GuildUserEntity();
        guildUserEntity.setGuildId(guildId);
        guildUserEntity.setUserId(userId);
        guildUserEntity.setVisitedAt(LocalDateTime.now());
        guildUserEntity.setCreatedAt(LocalDateTime.now());
        guildUserEntity.setModifiedAt(LocalDateTime.now());
        guildUserEntity.isDeleted = false;

        return  guildUserEntity;
    }

}
