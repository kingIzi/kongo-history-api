package com.kongo.history.api.kongohistoryapi.utils;

import lombok.Data;

@Data
public class ErrorResponse {
    private String code;
    private String message;

    public ErrorResponse() {
        this.code = AppConst._KEY_CODE_SUCCESS;
        this.message = "success";
    }
}
