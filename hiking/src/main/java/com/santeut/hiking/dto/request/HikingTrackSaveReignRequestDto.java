package com.santeut.hiking.dto.request;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HikingTrackSaveReignRequestDto {
    private Integer partyId;
    private List<TrackData> trackDataList;
}
