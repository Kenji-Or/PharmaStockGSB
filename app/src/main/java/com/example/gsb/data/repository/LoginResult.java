package com.example.gsb.data.repository;

public class LoginResult {
    private final boolean success;
    private final String error;

    public LoginResult(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getError() {
        return error;
    }
}
