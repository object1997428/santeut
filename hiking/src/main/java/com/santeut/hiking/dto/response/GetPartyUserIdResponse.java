package com.santeut.hiking.dto.response;

import lombok.*;

@Data @Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class GetPartyUserIdResponse {
    private int partyId;
    private int userId;
}
