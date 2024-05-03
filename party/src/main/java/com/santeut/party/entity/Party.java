package com.santeut.party.entity;

import com.santeut.party.dto.request.CreatePartyRequestDto;
import jakarta.persistence.*;
import java.time.format.DateTimeFormatter;
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
@Table(name = "party")
public class Party extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_id", nullable = false)
    private int partyId;

    @Column(name = "party_is_linked", nullable = false)
    private boolean isLinked;

    @Column(name = "course_id", nullable = false)
    private int courseId;

    @Column(name = "guild_id")
    private int guildId;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "party_mountain_name", length = 300, nullable = false)
    private String mountainName;

    @Column(name = "party_name", columnDefinition = "TEXT",nullable = false)
    private String partyName;

    @Column(name = "party_schedule", nullable = false)
    private LocalDateTime schedule;

    @Column(name = "party_place", columnDefinition = "TEXT",nullable = false)
    private String place;

    @Column(name = "party_status", length = 1, nullable = false)
    private char status;

    @Column(name = "party_max_participants", nullable = false)
    private int maxParticipants;

    @Column(name = "party_participants", nullable = false)
    private int participants;

    @Column(name = "party_started_at")
    private LocalDateTime started_at;

    @Column(name = "party_finished_at")
    private LocalDateTime finished_at;


    public static Party createEntity(int userId, CreatePartyRequestDto requestDto) {
        return Party.builder()
            .userId(userId)
            .mountainName(requestDto.getMountainName())
            .partyName(requestDto.getPartyName())
            .schedule(LocalDateTime.parse(requestDto.getSchedule(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
            .place(requestDto.getPlace())
            .status('B')
            .maxParticipants(requestDto.getMaxPeople())
            .build();
    }
}
