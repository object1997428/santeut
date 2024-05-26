package com.santeut.party.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class TrackData {
    private Integer userId;
    private List<LocationData> locationDataList;
}
