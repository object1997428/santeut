package com.santeut.mountain.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PartyCourseResponse {
  double distance;
  private List<LocationData> locationDataList;

  public PartyCourseResponse(double distance, List<LocationData> locationDataList) {
    this.distance = Math.round(distance * 100) / 100.0;
    this.locationDataList = locationDataList;
  }

}
