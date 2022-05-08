package com.praktikum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserResponse {
    private boolean success;
    private UserResponse user;
    private String accessToken;
    private String refreshToken;
}
