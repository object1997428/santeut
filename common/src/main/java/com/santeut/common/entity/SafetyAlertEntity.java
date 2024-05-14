package com.santeut.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.locationtech.jts.geom.Point;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "safety_alert")
@EntityListeners(AuditingEntityListener.class)
public class SafetyAlertEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "safety_alert_id")
    private Integer id;

    @NotNull
    @Column(name = "user_id")
    private int userId;

    @NotNull
    @Column(name = "safety_alert_title", length = 255)
    private String title;

    @NotNull
    @Column(name = "safety_alert_message")
    private String message;

    @NotNull
    @Column(name = "alert_point")
    private Point point;

    @CreatedDate
    @NotNull
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;


}
