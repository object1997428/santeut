package com.santeut.mountain.service;

import com.santeut.mountain.dto.response.MountainDetailResponseDto;
import com.santeut.mountain.dto.response.MountainInfoResponseDto;
import com.santeut.mountain.dto.response.MountainSearchResponseDto;
import com.santeut.mountain.dto.response.SearchResultResponse;
import java.util.List;

public interface MountainService {

  SearchResultResponse findByNameAndRegion(String name, String region);

  SearchResultResponse getMountainByViews();

  MountainDetailResponseDto findMountainById(int mountainId);

  MountainInfoResponseDto getMountainInfoById(int mountainId);
}
