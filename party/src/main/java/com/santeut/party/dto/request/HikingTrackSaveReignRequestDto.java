package com.santeut.party.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HikingTrackSaveReignRequestDto {
    private Integer partyId;
    private List<TrackData> trackDataList;
}
