package com.spatil.common.model;

public class Error {
    public Error(String message,int errorCode) {
        this.setMessage(message);
        this.setErrorCode(errorCode);
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    String message;
    int errorCode;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
