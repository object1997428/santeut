package com.santeut.party.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder @AllArgsConstructor
public class MountainCourseRequest {
    private List<Integer> courseIdList;
}
