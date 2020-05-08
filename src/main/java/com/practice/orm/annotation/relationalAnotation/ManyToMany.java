package com.practice.orm.annotation.relationalAnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ManyToMany {
    String primaryKey() default "";

    String nameTable() default "";

    JoinColumn joinColumn() default @JoinColumn;

    JoinColumn inverseJoinColumn() default @JoinColumn;

    FetchType fetch() default FetchType.LAZY;
}