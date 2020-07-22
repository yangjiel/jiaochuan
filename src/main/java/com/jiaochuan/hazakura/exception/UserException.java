package com.jiaochuan.hazakura.exception;

public class UserException extends Exception {
    public UserException() {
        super();
    }

    public UserException(Throwable e) {
        super(e);
    }

    public UserException(String message) {
        super(message);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }
}
