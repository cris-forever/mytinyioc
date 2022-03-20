package com.cris.myspring.applicationcontext;

public interface ApplicationContext {
    //Q:为什么不使用泛型呢？
    public Object getBean(String beanName);
}
