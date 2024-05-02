package com.santeut.auth.dto.response;

import com.santeut.auth.util.LevelUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public class GetUserLevelResponse {

    String userTierName;
    int userTierPoint;

}
