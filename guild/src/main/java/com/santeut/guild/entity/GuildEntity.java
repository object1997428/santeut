package com.santeut.guild.entity;

import com.santeut.guild.dto.request.CreateGuildRequest;
import com.santeut.guild.dto.request.PatchGuildInfoRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

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

    public static GuildEntity createGuild(CreateGuildRequest request, int userId){

        GuildEntity guildEntity = new GuildEntity();
        guildEntity.guildName = request.getGuildName();
        guildEntity.guildIsPrivate = request.isGuildIsPrivate();
        guildEntity.guildInfo = request.getGuildInfo();
        guildEntity.guildMember = 1;
        guildEntity.regionId = request.getRegionId();
        guildEntity.guildGender = request.getGuildGender();
        guildEntity.guildMinAge = request.getGuildMinAge();
        guildEntity.guildMaxAge = request.getGuildMaxAge();
        guildEntity.userId = userId;
        guildEntity.createdAt = LocalDateTime.now();
        guildEntity.modifiedAt = LocalDateTime.now();
        guildEntity.isDeleted = false;
        return  guildEntity;
    };

    public static GuildEntity patchGuild(PatchGuildInfoRequest request, GuildEntity guildEntity){

        guildEntity.guildName = request.getGuildName();
        guildEntity.guildInfo = request.getGuildInfo();
        guildEntity.guildIsPrivate = request.isGuildIsPrivate();
        guildEntity.guildMember = request.getGuildMember();
        guildEntity.regionId = request.getRegionId();
        guildEntity.guildGender = request.getGuildGender();
        guildEntity.guildMinAge = request.getGuildMinAge();
        guildEntity.guildMaxAge = request.getGuildMaxAge();
        guildEntity.modifiedAt = LocalDateTime.now();

        return guildEntity;
    }
}
