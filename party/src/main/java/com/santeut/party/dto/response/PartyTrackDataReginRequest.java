package com.santeut.party.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.santeut.party.dto.request.LocationData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder @AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PartyTrackDataReginRequest {
    private List<LocationData> locationDataList;
}
