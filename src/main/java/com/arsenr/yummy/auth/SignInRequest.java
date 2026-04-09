package com.arsenr.yummy.auth;

public record SignInRequest(
        String email,
        String password
) {
}
