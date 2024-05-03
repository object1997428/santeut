package com.santeut.party.entity;

import com.santeut.party.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "party_user")
public class PartyUser extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_user_id", nullable = false)
    private int partyUserId;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "party_id", nullable = false)
    private int partyId;

    @Column(name = "party_user_status", length = 1, nullable = false)
    private char status;

    @Column(name = "party_user_distance", nullable = false)
    private int distance;

    @Column(name = "party_user_best_height", nullable = false)
    private int bestHeight;

    @Column(name = "party_user_move_time", nullable = false)
    private int moveTime;

    @Column(name = "party_user_is_success"/*, nullable = false*/)//후순위
    private boolean isSuccess;

    @Column(name = "party_user_points")
    private String points;

    @Column(name = "party_user_started_at", nullable = false)
    private LocalDateTime started_at;
}
