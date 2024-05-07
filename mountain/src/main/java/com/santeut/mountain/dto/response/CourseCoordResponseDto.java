package com.santeut.mountain.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
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
}
