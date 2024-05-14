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
  @NoArgsConstructor
  public static class CourseSummary {

    public int courseId;
    public double distance;
    public List<LocationData> locationDataList;

    public CourseSummary(int courseId, double distance, List<LocationData> locationDataList) {
      this.courseId = courseId;
      this.distance = Math.round(distance * 100) / 100.0;
      this.locationDataList = locationDataList;
    }
  }

}
