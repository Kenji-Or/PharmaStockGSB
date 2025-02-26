package com.example.gsb.data.model;

public class Result<T> {
    private final boolean success;
    private final T data;
    private final String error;

    public Result(boolean success, T data, String error) {
        this.success = success;
        this.data = data;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public String getError() {
        return error;
    }
}
