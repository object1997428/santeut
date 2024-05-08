package com.santeut.party.dto.request;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class HikingSafetyRequest {
    String type; //fall_detection, health_risk, off_course
    int partyId;
    private Double lat;
    private Double lng;
    private LocalDateTime createdAt;
}
