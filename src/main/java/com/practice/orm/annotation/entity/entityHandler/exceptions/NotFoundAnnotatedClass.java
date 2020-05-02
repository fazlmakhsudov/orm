package com.practice.orm.annotation.entity.entityHandler.exceptions;

public class NotFoundAnnotatedClass extends Exception{

    public NotFoundAnnotatedClass() {
        super();
    }

    public NotFoundAnnotatedClass(String not_found_exception) {
        super(not_found_exception);
    }

}
