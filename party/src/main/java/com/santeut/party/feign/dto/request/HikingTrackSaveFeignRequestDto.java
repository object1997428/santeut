package com.santeut.party.feign.dto.request;

import com.santeut.party.dto.request.TrackData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HikingTrackSaveFeignRequestDto {
    private Integer partyId;
    private List<TrackData> trackDataList;
}
