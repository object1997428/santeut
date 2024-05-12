package com.santeut.party.feign.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoFeignResponseDto {

  public int userId;
  public String userLoginId;
  public String userNickname;
  public LocalDateTime createdAt;
  public LocalDateTime modifiedAt;
  public String userBirth;
  public String userGender;
  public LocalDateTime deletedAt;
  public Boolean isDeleted;
  public String userProfile;

}
