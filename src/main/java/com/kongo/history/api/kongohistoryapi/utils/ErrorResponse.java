package com.kongo.history.api.kongohistoryapi.utils;


public class ErrorResponse {
    private String code;
    private String message;

    public ErrorResponse() {
        this.code = AppConst._KEY_CODE_SUCCESS;
        this.message = "success";
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
