package com.kongo.history.api.kongohistoryapi.util;

public class ErrorResponse {
    private String code;
    private String message;

    public ErrorResponse() {
        this.code = AppConst._KEY_CODE_SUCCESS;
        this.message = AppConst._KEY_MSG_SUCCESS;
    }

    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
