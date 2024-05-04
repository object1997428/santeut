package com.santeut.mountain.service;

import com.santeut.mountain.common.exception.NotFoundException;
import com.santeut.mountain.dto.response.CourseInfoResponseDto;
import com.santeut.mountain.entity.MountainEntity;
import com.santeut.mountain.repository.CourseRepository;
import com.santeut.mountain.repository.MountainRepository;
import lombok.RequiredArgsConstructor;
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
}
