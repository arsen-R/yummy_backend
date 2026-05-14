package com.arsenr.yummy.token;

import lombok.Builder;

import java.util.Objects;

public class RefreshTokenRequest {
    private String refreshToken;

    public RefreshTokenRequest() {}

    public RefreshTokenRequest(String refreshToken) {}

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RefreshTokenRequest that = (RefreshTokenRequest) o;
        return Objects.equals(refreshToken, that.refreshToken);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(refreshToken);
    }

    @Override
    public String toString() {
        return "RefreshTokenRequest{" +
                "refreshToken='" + refreshToken + '\'' +
                '}';
    }
}
