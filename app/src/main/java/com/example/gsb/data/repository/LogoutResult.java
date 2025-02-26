package com.example.gsb.data.repository;

public class LogoutResult {
    private final boolean success;
    private final String error;

    public LogoutResult(boolean success, String error) {
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
