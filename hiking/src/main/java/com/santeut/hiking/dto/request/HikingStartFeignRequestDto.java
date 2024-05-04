package com.santeut.hiking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class HikingStartFeignRequestDto {
    private int partyId;
    private int userId;
}
