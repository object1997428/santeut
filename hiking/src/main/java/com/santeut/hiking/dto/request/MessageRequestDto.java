package com.santeut.hiking.dto.request;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageRequestDto {
    private String type;
    private Integer partyId;
    private Integer userId;
    private String message;
}
