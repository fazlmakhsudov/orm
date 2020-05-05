package com.practice.orm.annotation.relationalAnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface OneToMany {
    String mappedBy() default "";
    FetchType fetch() default FetchType.LAZY;
}
