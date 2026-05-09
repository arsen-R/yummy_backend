package com.arsenr.yummy.handler;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {
    private String error;
    private String message;
    private Integer status;
    private Map<String, String> details;
    private Instant timestamp;
    private String path;

    public static ErrorResponse of(int status, String error, String message) {
        return ErrorResponse.builder().status(status).error(error).message(message).timestamp(Instant.now()).build();
    }

    public static ErrorResponse of(int status, String error, String message, String path) {
        return ErrorResponse.builder()
                .status(status)
                .error(error)
                .message(message)
                .timestamp(Instant.now())
                .path(path)
                .build();
    }

    public static ErrorResponse of(int status, String error,
                                   String message, Map<String, String> details, String path) {
        return ErrorResponse.builder()
                .status(status)
                .error(error)
                .message(message)
                .details(details)
                .timestamp(Instant.now())
                .path(path)
                .build();
    }
}
