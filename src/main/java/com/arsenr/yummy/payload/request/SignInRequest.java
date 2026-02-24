package com.arsenr.yummy.payload.request;

public record SignInRequest(
        String email,
        String password
) {
}
