package com.santeut.guild.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BasicResponse {
     int status;
     Object data;
}
