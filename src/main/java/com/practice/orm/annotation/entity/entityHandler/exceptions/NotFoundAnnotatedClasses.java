package com.practice.orm.annotation.entity.entityHandler.exceptions;

public class NotFoundAnnotatedClasses extends Exception{

    public NotFoundAnnotatedClasses() {
        super();
    }

    public NotFoundAnnotatedClasses(String not_found_exception) {
        super(not_found_exception);
    }

}
