package com.santeut.mountain.service;

import com.santeut.mountain.dto.request.PartyTrackDataReginRequest;
import com.santeut.mountain.dto.response.AllCourseResponse;
import com.santeut.mountain.dto.response.CourseCoordResponseDto;
import com.santeut.mountain.dto.response.CourseInfoResponseDto;
import com.santeut.mountain.dto.response.PartyCourseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseService {

  Page<CourseInfoResponseDto> findCourseByMountainId(int mountainId, Pageable pageable);

  CourseCoordResponseDto findCourseCoordByCourseId(int courseId);

  PartyCourseResponse findCourseCoordsByCourseId(PartyTrackDataReginRequest request);

  AllCourseResponse findAllCourseByMountainId(int mountainId);
}
