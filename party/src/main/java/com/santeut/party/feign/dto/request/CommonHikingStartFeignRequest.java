package com.santeut.party.feign.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder @AllArgsConstructor
public class CommonHikingStartFeignRequest {
    private String type;
    private List<Integer> targetUserIds;
    private String title;
    private String message;
    private int partyId;
    private String dataSource;
    private String alamType; //POPUP, PUSH
}
