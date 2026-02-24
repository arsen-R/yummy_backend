package com.arsenr.yummy.user;

import java.time.Instant;

public record UserDto(
        Long id,
        String firstName,
        String lastName,
        String displayName,
        String userName,
        String bio,
        String email,
        Instant createdAt,
        Instant updatedAt,
        Boolean isAccountNonExpired,
        Boolean isAccountNonLocked,
        Boolean isCredentialsNonExpired,
        Boolean isEnabled
) {
}
