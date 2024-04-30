package com.santeut.mountain.service;

import com.santeut.mountain.dto.response.MountainSearchResponseDto;
import com.santeut.mountain.repository.MountainRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
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
}
