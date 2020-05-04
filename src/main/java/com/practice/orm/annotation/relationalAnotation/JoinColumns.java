package com.practice.orm.annotation.relationalAnotation;

import com.practice.orm.annotation.entity.Column;
import com.practice.orm.annotation.entity.Id;
import com.practice.orm.annotation.entity.Table;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JoinColumns {
    JoinColumn joinColumn();
}
