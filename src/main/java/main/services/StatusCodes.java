package main.services;

public enum StatusCodes {
    OK(200),

    ENTITY_NOT_FOUND(404),
    LOGIN_BUSY(422),
    USER_EXISTS(422),
    INVALID_SPEC(422),

    SQL_ERROR(500),
    INVALID_RESULT(42);

    private Integer code;

    StatusCodes(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
