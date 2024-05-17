package com.santeut.guild.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchGuildNameListResponse {
    List<GetDetailGuildResponse> searchList = new ArrayList<>();
}
