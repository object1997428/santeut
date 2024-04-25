package com.santeut.common.entity;

import com.santeut.common.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "comment")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private int commentId;

    @NotNull
    @Column(name = "comment_reference_type", length = 255)
    private String commentReferenceType;

    @NotNull
    @Column(name = "comment_reference_id")
    private int commentReferenceId;

    @NotNull
    @Column(name = "user_id")
    private int userId;

    @NotNull
    @Column(name = "comment_content", columnDefinition = "TEXT")
    private String commentContent;
}
