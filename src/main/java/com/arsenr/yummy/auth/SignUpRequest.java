package com.arsenr.yummy.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
        @Email(regexp = "(?i)[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-z0-9-]+\\.)+[a-z]{2,6}", message = "Invalid email")
        String email,
        @NotBlank(message = "Password shouldn't be blank")
        @Size(min = 8, message = "Password should contain at least 8 characters")
        String password,
        String firstName,
        String lastName,
        String bio,
        String displayName,
        String username
) {
}
