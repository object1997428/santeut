package com.santeut.guild.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "guild_request")
public class GuildRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int guildRequestId;
    @NotNull
    private int guildId;
    @NotNull
    private int userId;
    @NotNull
    private LocalDateTime createdAt;
    @NotNull
    private char status;
    @NotNull
    private LocalDateTime modifiedAt;

    public static GuildRequestEntity requestGuild(int guildId, int userId){

        GuildRequestEntity guildRequestEntity = new GuildRequestEntity();
        guildRequestEntity.setGuildId(guildId);
        guildRequestEntity.setUserId(userId);
        guildRequestEntity.setCreatedAt(LocalDateTime.now());
        guildRequestEntity.setStatus('R');
        guildRequestEntity.setModifiedAt(LocalDateTime.now());

        return guildRequestEntity;
    }
}
