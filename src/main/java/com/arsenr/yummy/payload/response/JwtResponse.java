package com.arsenr.yummy.payload.response;

import com.arsenr.yummy.user.UserDto;

public record JwtResponse(
        String token,
        String refreshToken
) {
}
