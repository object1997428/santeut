package com.santeut.mountain.dto.response;

import com.santeut.mountain.entity.CourseEntity;
import com.santeut.mountain.entity.MountainEntity;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;

@Getter
@Setter
@Builder
public class MountainDetailResponseDto {

  public String mountainName;
  public String address;
  public String description;
  public int height;
  public int courseCount;
  public double lat;
  public double lng;
  public int views;
  public List<Course> courses;

  public static MountainDetailResponseDto from(MountainEntity m) {
    return MountainDetailResponseDto.builder()
        .mountainName(m.getMountainName())
        .address(m.getAddress())
        .description(m.getDescription())
        .height(m.getHeight())
        .courseCount(m.getCourseEntityList().size())
        .lat(m.getMountainTop().getY())
        .lng(m.getMountainTop().getX())
        .views(m.getViews())
        .courses(m.getCourseEntityList().stream()
            .map(Course::from)
            .toList())
        .build();
  }

  @Getter
  @Setter
  @Builder
  static class Course {
    public int courseId;
    public String courseName;
    public String level;
    public double distance;
    public int upTime;
    public int downTime;
    public List<List<Double>> coursePoints;

    static Course from(CourseEntity entity) {

      return Course.builder()
          .courseId(entity.getCourseId())
          .courseName(entity.getCourseName())
          .level(entity.getCourseLevel())
          .distance(entity.getDistance())
          .upTime(entity.getUpTime())
          .downTime(entity.getDownTime())
          .coursePoints(convertGeometryToCoordinateList(entity.getCoursePoints()))
          .build();
    }

    private static List<List<Double>> convertGeometryToCoordinateList(Geometry geometry) {
      List<List<Double>> coordinateList = new ArrayList<>();

      if (geometry instanceof LineString) {
        convertLineStringToCoordinateList((LineString) geometry, coordinateList);
      } else if (geometry instanceof MultiLineString multiLineString) {
        for (int i = 0; i < multiLineString.getNumGeometries(); i++) {
          Geometry lineString = multiLineString.getGeometryN(i);
          if (lineString instanceof LineString) {
            convertLineStringToCoordinateList((LineString) lineString, coordinateList);
          }
        }
      }
      return coordinateList;
    }

    private static void convertLineStringToCoordinateList(LineString lineString,
        List<List<Double>> coordinateList) {
      Coordinate[] coordinates = lineString.getCoordinates();
      for (Coordinate coordinate : coordinates) {
        List<Double> point = new ArrayList<>();
        point.add(coordinate.y);  // 위도
        point.add(coordinate.x);  // 경도
        coordinateList.add(point);
      }
    }
  }

}
