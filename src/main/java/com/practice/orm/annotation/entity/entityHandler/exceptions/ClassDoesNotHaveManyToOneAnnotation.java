package com.practice.orm.annotation.entity.entityHandler.exceptions;

public class ClassDoesNotHaveManyToOneAnnotation extends Exception{
    public ClassDoesNotHaveManyToOneAnnotation() {
    }

    public ClassDoesNotHaveManyToOneAnnotation(String message) {
        super(message);
    }

    public ClassDoesNotHaveManyToOneAnnotation(String message, Throwable cause) {
        super(message, cause);
    }

    public ClassDoesNotHaveManyToOneAnnotation(Throwable cause) {
        super(cause);
    }

    public ClassDoesNotHaveManyToOneAnnotation(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
