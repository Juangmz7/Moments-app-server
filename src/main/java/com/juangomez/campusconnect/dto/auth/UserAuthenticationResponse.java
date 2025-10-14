package com.juangomez.campusconnect.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserAuthenticationResponse {

    private UUID id;

    private String email;

}
