package com.kongo.history.api.kongohistoryapi.utils;

import lombok.Data;

@Data
public class UtilityFormatter {
    public static void formatMessagesParamsError(HttpDataResponse<?> httpDataResponse, final ValueDataException e) {
        httpDataResponse.getStatus().setCode(e.getCode());
        httpDataResponse.getStatus().setMessage(e.getMessage());
    }

    public static void formatMessagesParamsError(HttpDataResponse<?> httpDataResponse, final String message) {
        httpDataResponse.getStatus().setCode(AppConst._KEY_CODE_INTERNAL_ERROR);
        httpDataResponse.getStatus().setMessage(message);
    }

    public static void formatMessagesParamsError(HttpDataResponse<?> httpDataResponse) {
        httpDataResponse.getStatus().setCode(AppConst._KEY_CODE_INTERNAL_ERROR);
        httpDataResponse.getStatus().setMessage("SOMETHING WENT WRONG");
    }

    public static void formatMessagesParamsError(HttpDataResponse<?> httpDataResponse, String message, String code) {
        httpDataResponse.getStatus().setCode(code);
        httpDataResponse.getStatus().setMessage(message);
    }
}
