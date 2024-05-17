package com.santeut.party.dto.response;

import com.santeut.party.dto.request.LocationData;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SelectedCourseResponse {
  public double distance;
  private List<LocationData> courseList;
}
