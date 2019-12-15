package main.models;

import main.services.StatusCodes;

public class Result<T> {
    private T value;
    private StatusCodes statusCode;
    private String statusMessage;

    public Result(T value, StatusCodes statusCode, String statusMessage) {
        this.value = value;
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    public Result(T value, StatusCodes statusCode) {
        this(value, statusCode, null);
    }

    public StatusCodes getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public T getValue() {
        return value;
    }

    public Boolean isValid() {
        return statusCode != null;
    }

    public static <U> Result<U> ofType(U type) {
        return new Result<>(null, null, null);
    }
}
