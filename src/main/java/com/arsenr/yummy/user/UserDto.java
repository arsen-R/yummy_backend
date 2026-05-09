package com.arsenr.yummy.user;

import com.arsenr.yummy.role.Role;

import java.time.Instant;
import java.util.Set;

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
        Set<Role> roles
) {
}
