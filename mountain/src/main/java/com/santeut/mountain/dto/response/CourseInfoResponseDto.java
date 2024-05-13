package com.santeut.mountain.dto.response;

import com.santeut.mountain.entity.CourseEntity;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class CourseInfoResponseDto {

  public List<CourseInfo> courseList;

  @Data
  @Builder
  public static class CourseInfo {

    public int courseId;
    public String courseName;
    public double distance;
    public String level;
    public int upTime;
    public int downTime;

    public static CourseInfo from(CourseEntity c) {
      return CourseInfo.builder()
          .courseId(c.getCourseId())
          .courseName(c.getCourseName())
          .distance(c.getDistance())
          .level(c.getCourseLevel())
          .upTime(c.getUpTime())
          .downTime(c.getDownTime())
          .build();
    }

  }

}
