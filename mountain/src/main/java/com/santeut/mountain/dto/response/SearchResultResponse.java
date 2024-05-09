package com.santeut.mountain.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchResultResponse {

  List<MountainSearchResponseDto> result;

}
