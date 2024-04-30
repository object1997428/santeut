package com.santeut.mountain.service;

import com.santeut.mountain.dto.response.MountainSearchResponseDto;
import java.util.List;

public interface MountainService {

  List<MountainSearchResponseDto> findByName(String name);
}
