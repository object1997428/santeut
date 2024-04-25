package com.santeut.community.entity;

import com.santeut.community.common.BaseEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Post extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(name = "user_id")
    @NotNull
    private int userId;

    @Column(name = "post_title")
    @NotNull
    private String postTitle;

    @Column(name = "post_content")
    @NotNull
    private String postContent;

    @Column(name = "post_type")
    @NotNull
    private int postType;

    @Column(name = "user_party_id")
    @NotNull
    private int userPartyId;
}
