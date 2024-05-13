package com.santeut.guild.feign.dto;


import com.santeut.guild.dto.response.PartyMemberInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartyMemberInfoResponse {

    List<PartyMemberInfo> partyMembers;

}
