package com.santeut.party.feign.dto.response;

import com.santeut.party.dto.request.LocationData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HikingTrackResponse {
    private List<LocationData> courseList;
}
