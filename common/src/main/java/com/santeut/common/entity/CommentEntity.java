package com.santeut.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comment")
public class CommentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Integer id;

    @NotNull
    @Column(name = "comment_reference_type", length = 1)
    private Character commentReferenceType;

    @NotNull
    @Column(name = "comment_reference_id")
    private Integer commentReferenceId;

    @NotNull
    @Column(name = "user_id")
    private Integer userId;

    @NotNull
    @Column(name = "comment_content", columnDefinition = "TEXT")
    private String commentContent;
}
