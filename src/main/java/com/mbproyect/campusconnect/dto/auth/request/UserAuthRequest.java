package com.mbproyect.campusconnect.dto.auth.request;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserAuthRequest {

    @Email
    private String email;

}
