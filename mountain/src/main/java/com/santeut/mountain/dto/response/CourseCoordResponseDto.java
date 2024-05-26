package com.santeut.mountain.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseCoordResponseDto {

  public int coordCnt;
  public double distance;
  public List<Coord> coordPoints;

  @Data
  @AllArgsConstructor
  public static class Coord {
    public double lat;
    public double lng;
  }

  public CourseCoordResponseDto(int coordCnt, double distance, List<Coord> coordPoints) {
    this.coordCnt = coordCnt;
    this.distance = Math.round(distance * 100) / 100.0;
    this.coordPoints = coordPoints;
  }
}
