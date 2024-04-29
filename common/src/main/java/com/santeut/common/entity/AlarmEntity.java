package com.santeut.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class AlarmEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Integer alarmId;

    @NotNull
    @Column(name = "user_id")
    private int userId;

    @NotNull
    @Column(name = "reference_type", length = 255)
    private String referenceType;

    @NotNull
    @Column(name = "reference_id")
    private int referenceId;

    @NotNull
    @Column(name = "alarm_title", length = 255)
    private String alarmTitle;

    @NotNull
    @Column(name = "alarm_content", length = 255)
    private String alarmContent;
}
