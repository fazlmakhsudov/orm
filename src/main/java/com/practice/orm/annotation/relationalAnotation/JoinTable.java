package com.practice.orm.annotation.relationalAnotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JoinTable {
    String name() default "";
    JoinColumn joinColumn();
    JoinColumn inverseJoinColumn();
}
