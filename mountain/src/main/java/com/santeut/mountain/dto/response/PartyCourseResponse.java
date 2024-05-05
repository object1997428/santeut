package com.santeut.mountain.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PartyCourseResponse {
  private List<LocationData> locationDataList;

}
