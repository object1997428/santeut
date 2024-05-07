package com.santeut.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity(name = "image")
public class ImageEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private int id;

    @NotNull
    @Column(name = "image_reference_type", length = 1)
    private Character imageReferenceType;

    @NotNull
    @Column(name = "image_reference_id")
    private int imageReferenceId;

    @NotNull
    @Column(name = "image_url", length = 255)
    private String imageUrl;
}
