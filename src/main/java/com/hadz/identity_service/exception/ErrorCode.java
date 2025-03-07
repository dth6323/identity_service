package com.hadz.identity_service.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999,"Uncategorized error"),
    INVALID_KEY(1001,"Invalid key"),
    USER_EXISTED(1002,"User existed"),
    INVALID_PASSWORD(1004,"Password must be at least 8 characters"),
    USERNAME_INVALID(1003,"Username must be at least 3 characters"),;
    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;
}
