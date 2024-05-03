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
@Table(name = "guild")
public class GuildEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int guildId;

    @NotNull
    @Column(length = 45)
    private String guildName;

    @NotNull
    private boolean guildIsPrivate;

    private String guildProfile;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String guildInfo;

    @NotNull
    private int guildMember;

    @NotNull
    private int regionId;

    @NotNull
    private char guildGender;

    @NotNull
    private int guildMinAge;

    @NotNull
    private int guildMaxAge;

    @NotNull
    private int userId;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private LocalDateTime modifiedAt;

    @NotNull
    private boolean isDeleted;

    private LocalDateTime deletedAt;
}
