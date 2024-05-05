package com.santeut.mountain.dto.response;

import com.santeut.mountain.entity.CourseEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseInfoResponseDto {

  public int courseId;
  public String courseName;
  public double distance;
  public String level;
  public int upTime;
  public int downTime;

  public static CourseInfoResponseDto from(CourseEntity c) {
    return CourseInfoResponseDto.builder()
        .courseId(c.getCourseId())
        .courseName(c.getCourseName())
        .distance(c.getDistance())
        .level(c.getCourseLevel())
        .upTime(c.getUpTime())
        .downTime(c.getDownTime())
        .build();
  }

}
