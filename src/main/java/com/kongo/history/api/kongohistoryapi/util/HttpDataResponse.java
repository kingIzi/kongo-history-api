package com.kongo.history.api.kongohistoryapi.util;

public class HttpDataResponse<T> {
    private ErrorResponse status;
    private T response;

    public HttpDataResponse() {
        status = new ErrorResponse();
    }

    public ErrorResponse getStatus() {
        return status;
    }

    public void setStatus(ErrorResponse status) {
        this.status = status;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }
}
