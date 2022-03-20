package com.cris.myspring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) //使用在类上
@Retention(RetentionPolicy.RUNTIME) //运行时注解
public @interface Component {
    String value() default ""; //可以在使用注解的同时往注解传值，如果没有default就是必须传值
}
