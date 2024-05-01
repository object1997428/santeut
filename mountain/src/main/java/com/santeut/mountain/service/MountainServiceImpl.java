package com.santeut.mountain.service;

import com.santeut.mountain.common.exception.NotFoundException;
import com.santeut.mountain.dto.response.MountainDetailResponseDto;
import com.santeut.mountain.dto.response.MountainSearchResponseDto;
import com.santeut.mountain.entity.MountainEntity;
import com.santeut.mountain.repository.MountainRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MountainServiceImpl implements MountainService {

  private final MountainRepository mountainRepository;

  @Override
  public List<MountainSearchResponseDto> findByName(String name) {

    return mountainRepository.findAllByMountainNameContainingOrderByViewsDesc(name)
        .stream()
        .map(MountainSearchResponseDto::from)
        .collect(Collectors.toList());

  }

  @Override
  public List<MountainSearchResponseDto> getMountainByViews() {
    return mountainRepository.findTop10ByOrderByViewsDesc()
        .stream()
        .map(MountainSearchResponseDto::from)
        .collect(Collectors.toList());
  }

  @Override
  public MountainDetailResponseDto findMountainById(int mountainId) {
    MountainEntity mountain = mountainRepository.findById(mountainId)
        .orElseThrow(() -> new NotFoundException("해당 산이 존재하지 않습니다"));
    return MountainDetailResponseDto.from(mountain);
  }

}
