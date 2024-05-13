package com.santeut.guild.feign.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartyMemberInfoRequest {

    public List<Integer> userIdList;
}
