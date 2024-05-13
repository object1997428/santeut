package com.santeut.mountain.service;

import com.santeut.mountain.common.exception.NotFoundException;
import com.santeut.mountain.common.util.GeometryUtils;
import com.santeut.mountain.dto.request.PartyTrackDataReginRequest;
import com.santeut.mountain.dto.response.AllCourseResponse;
import com.santeut.mountain.dto.response.AllCourseResponse.CourseSummary;
import com.santeut.mountain.dto.response.CourseCoordResponseDto;
import com.santeut.mountain.dto.response.CourseCoordResponseDto.Coord;
import com.santeut.mountain.dto.response.CourseInfoResponseDto;
import com.santeut.mountain.dto.response.CourseInfoResponseDto.CourseInfo;
import com.santeut.mountain.dto.response.LocationData;
import com.santeut.mountain.dto.response.PartyCourseResponse;
import com.santeut.mountain.entity.CourseEntity;
import com.santeut.mountain.entity.MountainEntity;
import com.santeut.mountain.repository.CourseRepository;
import com.santeut.mountain.repository.MountainRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseServiceImpl implements CourseService {

  private final CourseRepository courseRepository;
  private final MountainRepository mountainRepository;

  @Override
  public CourseInfoResponseDto findCourseByMountainId(int mountainId) {
    MountainEntity mountain = mountainRepository.findById(mountainId)
        .orElseThrow(() -> new NotFoundException("해당 산은 존재하지 않습니다"));

    return new CourseInfoResponseDto(courseRepository.findAllByMountainId(mountain)
        .stream().map(CourseInfo::from).collect(Collectors.toList()));
  }

  @Override
  public CourseCoordResponseDto findCourseCoordByCourseId(int courseId) {
    CourseEntity course = courseRepository.findById(courseId)
        .orElseThrow(() -> new NotFoundException("해당 등산로는 존재하지 않습니다"));
    if (course.getCoursePoints() == null) {
      return new CourseCoordResponseDto(0, course.getDistance(), null);
    } else {
      List<Coord> coordPoints = convertGeometryToCoordinateList(course.getCoursePoints());
      return new CourseCoordResponseDto(coordPoints.size(), course.getDistance(), coordPoints);
    }

  }

  @Override
  public PartyCourseResponse findCourseCoordsByCourseId(PartyTrackDataReginRequest request) {
    List<LocationData> coords = new ArrayList<>();
    double totalDistance = 0L;

    log.info("[mountain] 등산로 좌표 모음 조회");
    for(int courseId : request.getCourseIdList()) {
      CourseEntity course = courseRepository.findById(courseId)
              .orElseThrow(() -> new NotFoundException("해당 등산로는 존재하지 않습니다"));
      log.info(course.getCourseId()+"번 등산로");
      totalDistance += course.getDistance();
      coords.addAll(GeometryUtils.convertGeometryToListLocationData(course.getCoursePoints()));
    }
    log.info("등산로 개수: "+coords.size());
    return new PartyCourseResponse(totalDistance, coords);
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

  @Override
  public AllCourseResponse findAllCourseByMountainId(int mountainId) {
    MountainEntity mountain = mountainRepository.findById(mountainId)
        .orElseThrow(() -> new NotFoundException("해당 산은 존재하지 않습니다"));

    List<CourseSummary> courseList = new ArrayList<>();
    for (CourseEntity course : mountain.getCourseEntityList()) {
      courseList.add(new CourseSummary(course.getCourseId(), course.getDistance(),
          GeometryUtils.convertGeometryToListLocationData(course.getCoursePoints())));
    }
    return new AllCourseResponse(courseList);
  }
}
