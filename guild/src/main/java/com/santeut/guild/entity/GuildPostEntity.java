package com.santeut.guild.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "guild_post")
public class GuildPostEntity extends BaseEntity {

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
}
