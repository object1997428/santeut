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
@Table(name = "guild_post")
public class GuildPostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int guildPostId;

    @NotNull
    private int categoryId;

    @NotNull
    private int guildId;

    @NotNull
    private int userId;

    @NotNull
    @Column(length = 150)
    private String guildPostTitle;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String guildPostContent;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private LocalDateTime modifiedAt;

    @NotNull
    private boolean isDeleted;

    private LocalDateTime deletedAt;
}
