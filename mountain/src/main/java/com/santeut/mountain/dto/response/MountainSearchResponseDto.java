package com.santeut.mountain.dto.response;

import com.santeut.mountain.entity.MountainEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MountainSearchResponseDto {

  public String mountainName;
  public String regionName;
  public int height;
  public int courseCount;
  public boolean isTop100;

  public static MountainSearchResponseDto from(MountainEntity e) {
    return MountainSearchResponseDto.builder()
        .mountainName(e.getMountainName())
        .regionName(e.getRegionName())
        .height(e.getHeight())
        .courseCount(e.getCourseEntityList().size())
        .isTop100(e.isTop100())
        .build();
  }

}
