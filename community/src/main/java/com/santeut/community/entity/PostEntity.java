package com.santeut.community.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Table(name = "post")
public class PostEntity extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Integer id;

    @Column(name = "user_id")
    @NotNull
    private Integer userId;

    @Setter
    @Column(name = "post_title")
    @NotNull
    private String postTitle;

    @Setter
    @Column(name = "post_content")
    @NotNull
    private String postContent;

    @Column(name = "post_type", length = 1)
    @NotNull
    private Character postType;

    @Column(name = "user_party_id")
    @NotNull
    private Integer userPartyId;
}

