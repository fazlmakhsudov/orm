package com.practice.orm.annotation.entity.entityHandler.exceptions;

public class NotFoundAnnotatedClass extends Exception {

    public NotFoundAnnotatedClass() {
        super();
    }

    public NotFoundAnnotatedClass(String not_found_exception) {
        super(not_found_exception);
    }

    public NotFoundAnnotatedClass(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundAnnotatedClass(Throwable cause) {
        super(cause);
    }

    public NotFoundAnnotatedClass(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
