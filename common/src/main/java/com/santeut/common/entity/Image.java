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
public class Image extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private int imageId;

    @NotNull
    @Column(name = "image_reference_type", length = 255)
    private String imageReferenceType;

    @NotNull
    @Column(name = "image_reference_id")
    private int imageReferenceId;

    @NotNull
    @Column(name = "image_url", length = 255)
    private String imageUrl;
}
