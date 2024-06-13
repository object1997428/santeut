package com.santeut.hiking.dto.request;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationData {
    public double lat;
    public double lng;
}
