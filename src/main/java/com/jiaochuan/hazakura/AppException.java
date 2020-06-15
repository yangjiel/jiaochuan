package com.jiaochuan.hazakura;

public class AppException extends Exception {
    public AppException() {
        super();
    }

    public AppException(Throwable e) {
        super(e);
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
    }
}
