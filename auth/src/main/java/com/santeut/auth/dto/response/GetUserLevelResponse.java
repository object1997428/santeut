package com.santeut.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class GetUserLevelResponse {

    int userTierId;
    String userTierName;
    int userTierPoint;
}
