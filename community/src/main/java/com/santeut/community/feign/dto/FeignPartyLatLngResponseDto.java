package com.santeut.community.feign.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeignPartyLatLngResponseDto {
    private List<Point> courseList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Point {
        private double lat;
        private double lng;
    }
}
