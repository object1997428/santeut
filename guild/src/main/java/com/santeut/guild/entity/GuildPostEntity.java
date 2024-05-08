package com.santeut.guild.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "guild_post")
public class GuildPostEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guild_post_id")
    private int id;

    @NotNull
    @Column(name = "category_id")
    private int categoryId;

    @NotNull
    @Column(name = "guild_id")
    private int guildId;

    @NotNull
    @Column(name = "user_id")
    private int userId;

    @NotNull
    @Column(name = "guild_post_title", length = 150)
    private String guildPostTitle;

    @NotNull
    @Column(name = "guild_post_content", columnDefinition = "TEXT")
    private String guildPostContent;

    @NotNull
    @Column(name = "hit_cnt", columnDefinition = "INTEGER DEFAULT 0")
    private int hitCnt;
}