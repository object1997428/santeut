package com.santeut.party.dto.response;

import com.santeut.party.dto.request.LocationData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.locationtech.jts.geom.Geometry;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class HikingStartResponse {
    private List<LocationData> courseList;
}
