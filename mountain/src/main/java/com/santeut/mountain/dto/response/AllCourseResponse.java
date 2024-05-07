package com.santeut.mountain.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllCourseResponse {

  public List<CourseSummary> course;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class CourseSummary {

    public int courseId;
    public double distance;
    public List<LocationData> locationDataList;

  }

}
