package com.santeut.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
public class UpdatePasswordRequest {

    String currentPassword;
    String updatePassword;
}
