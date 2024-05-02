package com.santeut.mountain.service;

import com.santeut.mountain.dto.response.MountainDetailResponseDto;
import com.santeut.mountain.dto.response.MountainSearchResponseDto;
import java.util.List;

public interface MountainService {

  List<MountainSearchResponseDto> findByNameAndRegion(String name, String region);

  List<MountainSearchResponseDto> getMountainByViews();

  MountainDetailResponseDto findMountainById(int mountainId);
}
