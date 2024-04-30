package com.santeut.auth.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_tier")
public class UserTierEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userTierId;

    @NotNull
    @Column(length = 9)
    private String userTierName;

    @NotNull
    private int userTierPoint;

    @NotNull
    private int userId;
}
