package main.services;

public class Result<T> {
    private T value;
    private StatusCodes statusCode;
    private String statusMessage;

    public Result(T value, StatusCodes statusCode, String statusMessage) {
        this.value = value;
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
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
}
