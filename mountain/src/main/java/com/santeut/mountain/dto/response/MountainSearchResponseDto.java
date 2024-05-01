package com.santeut.mountain.dto.response;

import com.santeut.mountain.entity.MountainEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MountainSearchResponseDto {

  public int mountainId;
  public String mountainName;
  public String regionName;
  public int height;
  public int courseCount;
  public boolean isTop100;

  public static MountainSearchResponseDto from(MountainEntity m) {
    return MountainSearchResponseDto.builder()
        .mountainId(m.getMountainId())
        .mountainName(m.getMountainName())
        .regionName(m.getRegionName())
        .height(m.getHeight())
        .courseCount(m.getCourseEntityList().size())
        .isTop100(m.isTop100())
        .build();
  }

}
