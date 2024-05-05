package com.santeut.mountain.service;

import com.santeut.mountain.common.exception.NotFoundException;
import com.santeut.mountain.dto.response.CourseCoordResponseDto;
import com.santeut.mountain.dto.response.CourseCoordResponseDto.Coord;
import com.santeut.mountain.dto.response.CourseInfoResponseDto;
import com.santeut.mountain.entity.CourseEntity;
import com.santeut.mountain.entity.MountainEntity;
import com.santeut.mountain.repository.CourseRepository;
import com.santeut.mountain.repository.MountainRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

  private final CourseRepository courseRepository;
  private final MountainRepository mountainRepository;

  @Override
  public Page<CourseInfoResponseDto> findCourseByMountainId(int mountainId, Pageable pageable) {
    MountainEntity mountain = mountainRepository.findById(mountainId)
        .orElseThrow(() -> new NotFoundException("해당 산은 존재하지 않습니다"));
    return courseRepository.findAllByMountainId(mountain, pageable)
        .map(CourseInfoResponseDto::from);
  }

  @Override
  public CourseCoordResponseDto findCourseCoordByCourseId(int courseId) {
    CourseEntity course = courseRepository.findById(courseId)
        .orElseThrow(() -> new NotFoundException("해당 등산로는 존재하지 않습니다"));
    if (course.getCoursePoints() == null) {
      return new CourseCoordResponseDto(0, null);
    } else {
      List<Coord> coordPoints = convertGeometryToCoordinateList(course.getCoursePoints());
      return new CourseCoordResponseDto(coordPoints.size(), coordPoints);
    }

  }

  private static List<Coord> convertGeometryToCoordinateList(Geometry geometry) {
    List<Coord> coordinateList = new ArrayList<>();

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
      List<Coord> coordinateList) {
    Coordinate[] coordinates = lineString.getCoordinates();
    for (Coordinate coordinate : coordinates) {
      coordinateList.add(new Coord(coordinate.y, coordinate.x));
    }
  }

}
