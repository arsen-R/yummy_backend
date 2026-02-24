package com.arsenr.yummy.payload.response;

import lombok.Builder;

import java.time.Instant;
import java.util.Map;

@Builder
public record ErrorResponse(
        String error,
        String message,
        Integer status,
        Map<String, String> details,
        Instant timestamp
) {
}
