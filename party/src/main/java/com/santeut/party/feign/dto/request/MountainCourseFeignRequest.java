package com.santeut.party.feign.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder @AllArgsConstructor
public class MountainCourseFeignRequest {
    private List<Integer> courseIdList;
}
