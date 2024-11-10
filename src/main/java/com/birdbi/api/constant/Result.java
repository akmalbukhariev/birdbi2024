package com.birdbi.api.constant;

public enum Result {
    SUCCESS(100, "Success."),
    FAILED(101, "Failed."),
    USER_EXIST(140, "User exist"),
    USER_NOT_EXIST(141, "User is not exist"),
    USER_PASSWORD_NOT_MATCHED(142, "Password is not matched!"),
    TOKEN_INVALID(200, "Invalid token information."),
    TOKEN_EXPIRED_TIME(201, "This token is expired."),
    TOKEN_UNSUPPORTED_JWT(202, "Unsupported token information."),
    LOGIN_INVALID_TOKEN(250, "Token information cannot be verified."),
    AUTHENTICATION_ERROR(300, "Your authentication information cannot be verified."),
    SERVER_ERROR(301, "A system error has occurred. Please contact your administrator."),
    TOKEN_EMPTY(360, "Empty token");

    private int code;
    private String message;

    private Result(int code, String msg) {
        this.code = code;
        this.message = msg;
    }

    public String getCodeToString() {
        return Integer.toString(this.code);
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public Result getResult(int code) {
        Result[] var2 = values();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Result result = var2[var4];
            if (result.getCode() == code) {
                return result;
            }
        }

        return null;
    }
}
