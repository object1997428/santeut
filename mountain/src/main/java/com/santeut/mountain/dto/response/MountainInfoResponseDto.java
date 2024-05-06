package com.santeut.mountain.dto.response;

import com.santeut.mountain.entity.MountainEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MountainInfoResponseDto {

  public String mountainName;
  public String address;
  public String description;
  public int height;
  public int courseCount;
  public double lat;
  public double lng;
  public int views;
  public String image;

  public static MountainInfoResponseDto from(MountainEntity m) {
    return MountainInfoResponseDto.builder()
        .mountainName(m.getMountainName())
        .address(m.getAddress())
        .description(m.getDescription())
        .height(m.getHeight())
        .courseCount(m.getCourseEntityList().size())
        .lat(m.getMountainTop().getY())
        .lng(m.getMountainTop().getX())
        .views(m.getViews())
        .image(m.getImage())
        .build();
  }

}
