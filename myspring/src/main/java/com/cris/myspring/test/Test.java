package com.cris.myspring.test;

import com.cris.myspring.annotation.Component;
import com.cris.myspring.applicationcontext.impl.MyAnnotationConfigApplicationContext;
import lombok.Data;

import java.lang.reflect.InvocationTargetException;

public class Test {
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        MyAnnotationConfigApplicationContext myAnnotationConfigApplicationContext =
                new MyAnnotationConfigApplicationContext("com.cris.myspring.entity");
        System.out.println(myAnnotationConfigApplicationContext.getBean("e"));
        System.out.println(myAnnotationConfigApplicationContext.getBean("f"));
    }
}
