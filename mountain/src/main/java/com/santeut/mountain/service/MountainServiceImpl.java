package com.santeut.mountain.service;

import com.santeut.mountain.common.exception.NotFoundException;
import com.santeut.mountain.dto.response.MountainDetailResponseDto;
import com.santeut.mountain.dto.response.MountainInfoResponseDto;
import com.santeut.mountain.dto.response.MountainSearchResponseDto;
import com.santeut.mountain.entity.MountainEntity;
import com.santeut.mountain.repository.MountainRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MountainServiceImpl implements MountainService {

  private final MountainRepository mountainRepository;

  @Override
  public List<MountainSearchResponseDto> findByNameAndRegion(String name, String region) {
    List<MountainEntity> mountainEntityList = new ArrayList<>();
    if(name!=null && region != null) {
      // region 지역 내 산 이름에 name을 포함한 산 검색
      mountainEntityList = mountainRepository.findAllByMountainNameContainingAndRegionNameLike(name, region);
    } else if(name!=null) {
      // 전국, 산 이름에 name을 포함한 산 검색
      mountainEntityList = mountainRepository.findAllByMountainNameContaining(name);
    } else if(region !=null) {
      // region 지역 내 모든 산 검색
      mountainEntityList = mountainRepository.findAllByRegionNameLike(region);
    } else {
      // 전국 산 검색
      mountainEntityList = mountainRepository.findAll();
    }

    return mountainEntityList
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

  @Override
  public MountainInfoResponseDto getMountainInfoById(int mountainId) {
    MountainEntity mountain = mountainRepository.findById(mountainId)
        .orElseThrow(() -> new NotFoundException("해당 산이 존재하지 않습니다"));
    return MountainInfoResponseDto.from(mountain);
  }
}
