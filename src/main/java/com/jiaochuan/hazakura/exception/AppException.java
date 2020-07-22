package com.jiaochuan.hazakura.exception;

public class AppException extends Exception {
    public AppException() {
        super();
    }

    public AppException(Throwable e) {
        super(e);
    }

    public AppException(String message) {
        super(message);
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
    }
}
