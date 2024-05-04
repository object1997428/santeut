package com.santeut.mountain.service;

import com.santeut.mountain.dto.response.CourseInfoResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseService {

  Page<CourseInfoResponseDto> findCourseByMountainId(int mountainId, Pageable pageable);
}
