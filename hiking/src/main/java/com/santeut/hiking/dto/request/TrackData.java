package com.santeut.hiking.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class TrackData {
    private Integer userId;
    private List<LocationData> locationDataList;
}
