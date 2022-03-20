package com.cris.myspring.applicationcontext.impl;

import com.cris.myspring.annotation.Autowired;
import com.cris.myspring.annotation.Component;
import com.cris.myspring.annotation.Qualifier;
import com.cris.myspring.annotation.Value;
import com.cris.myspring.applicationcontext.ApplicationContext;
import com.cris.myspring.beandefinition.BeanDefinition;
import com.cris.myspring.tools.MyTools;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MyAnnotationConfigApplicationContext implements ApplicationContext {
    //ioc用来存放对象
    HashMap<String, Object> ioc = new HashMap<>();

    /**
     * 自动注入
     *
     * @param beanDefinitions
     */
    public void autoWired(Set<BeanDefinition> beanDefinitions) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Iterator<BeanDefinition> iterator = beanDefinitions.iterator();
        while (iterator.hasNext()) {
            BeanDefinition beanDefinition = iterator.next();
            Class clazz = beanDefinition.getBeanClass();
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                Autowired autowiredAnnotation = declaredField.getAnnotation(Autowired.class);
                if (autowiredAnnotation != null) {
//                    System.out.println(autowiredAnnotation);
                    Qualifier qualifierAnnotation = declaredField.getAnnotation(Qualifier.class);
                    if (qualifierAnnotation != null) {
                        String value = qualifierAnnotation.value();
//                        System.out.println(value);
//                        Object bean = ioc.get(value);
                        Object bean = getBean(value);
                        //依然是通过set注入需要获得set方法
                        //这个获取方法名的功能可以单独开发一个函数
//                        Object obj = clazz.getConstructor().newInstance();
                        //注意：只能从ioc里面取，不能从使用class创建，不然注入不了。
                        Object object = getBean(beanDefinition.getBeanName());
//                        String fieldName = declaredField.getName();
//                        String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                        String methodName = getSetMethodName(declaredField);
                        Method method = clazz.getMethod(methodName, declaredField.getType());
                        //obj为属性的所属对象，bean为需要注入的对象
                        method.invoke(object, bean);
                    } else {
                        for (String s : ioc.keySet()) {
                            if (ioc.get(s).getClass() == declaredField.getType()){
                                String fieldName = declaredField.getName();
                                String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                                Method method = clazz.getMethod(methodName, declaredField.getType());
                                Object obj = clazz.getConstructor().newInstance();
                                method.invoke(obj, ioc.get(s));
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
//    public void autowireObject(Set<BeanDefinition> beanDefinitions){
//        Iterator<BeanDefinition> iterator = beanDefinitions.iterator();
//        while (iterator.hasNext()) {
//            BeanDefinition beanDefinition = iterator.next();
//            Class clazz = beanDefinition.getBeanClass();
//            Field[] declaredFields = clazz.getDeclaredFields();
//            for (Field declaredField : declaredFields) {
//                Autowired annotation = declaredField.getAnnotation(Autowired.class);
//                if(annotation!=null){
//                    Qualifier qualifier = declaredField.getAnnotation(Qualifier.class);
//                    if(qualifier!=null){
//                        //byName
//                        try {
//                            String beanName = qualifier.value();
//                            Object bean = getBean(beanName);
//                            String fieldName = declaredField.getName();
//                            String methodName = "set"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1);
//                            Method method = clazz.getMethod(methodName, declaredField.getType());
//                            Object object = getBean(beanDefinition.getBeanName());
//                            method.invoke(object, bean);
//                        } catch (NoSuchMethodException e) {
//                            e.printStackTrace();
//                        } catch (IllegalAccessException e) {
//                            e.printStackTrace();
//                        } catch (InvocationTargetException e) {
//                            e.printStackTrace();
//                        }
//                    }else{
//                        //byType
//                    }
//                }
//            }
//        }
//    }

    /**
     * 扫描这个包的所有类
     *
     * @param pack
     */
    public MyAnnotationConfigApplicationContext(String pack) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Set<BeanDefinition> beanDefinitions =
                findBeanDefinition(pack);
        createObject(beanDefinitions);
        autoWired(beanDefinitions);
    }

    /**
     * 通过beanDefinition集合创建实例
     *
     * @param beanDefinitions
     */
    public void createObject(Set<BeanDefinition> beanDefinitions) {
        Iterator<BeanDefinition> iterator = beanDefinitions.iterator();
        while (iterator.hasNext()) {
            BeanDefinition beanDefinition = iterator.next();
            Class clazz = beanDefinition.getBeanClass();
            String beanName = beanDefinition.getBeanName();
            try {
                //创建的对象
                Object object = clazz.getConstructor().newInstance();
                //完成属性的赋值
                Field[] declaredFields = clazz.getDeclaredFields();
                for (Field declaredField : declaredFields) {
                    Value valueAnnotation = declaredField.getAnnotation(Value.class);
                    if (valueAnnotation != null) {
                        String value = valueAnnotation.value();
//                        String fieldName = declaredField.getName();
//                        String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                        String methodName = getSetMethodName(declaredField);
                        Method method = clazz.getMethod(methodName, declaredField.getType());
                        //完成数据类型转换
                        Object val = null;
                        switch (declaredField.getType().getName()) {
                            case "java.lang.Integer":
                                val = Integer.parseInt(value);
                                break;
                            case "java.lang.String":
                                val = value;
                                break;
                            case "java.lang.Float":
                                val = Float.parseFloat(value);
                                break;
                        }
                        method.invoke(object, val);
                    }
                }
                //存入缓存
                ioc.put(beanName, object);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 1、获得指定包下的所有类
     * 2、找到添加了注解的类
     * 3、将其封装成beanDefinition对象
     *
     * @param pack
     * @return
     */
    public Set<BeanDefinition> findBeanDefinition(String pack) {

        Set<Class<?>> classes = MyTools.getClasses(pack);

        Iterator<Class<?>> iterator = classes.iterator();
        Set<BeanDefinition> beanDefinitions = new HashSet<>();
        while (iterator.hasNext()) {
            Class<?> clazz = iterator.next();
            //通过getAnnotation方法获取相应注解
            Component componentAnnotation = clazz.getAnnotation(Component.class);
            //获取component括号里的值，如果没有默认类的首字母小写作为baneName
            String beanName = componentAnnotation.value();
            if (beanName.equals("")) {
                //获取类名首字母小写
                String className = clazz.getName().replaceAll(clazz.getPackage().getName() + ".", "");
                beanName = className.substring(0, 1).toLowerCase() + className.substring(1);
            }
            BeanDefinition beanDefinition = new BeanDefinition(clazz, beanName);
            beanDefinitions.add(beanDefinition);
        }
        return beanDefinitions;
    }

    @Override
    public Object getBean(String beanName) {
        return ioc.get(beanName);
    }

    /**
     * 返回属性对应的set方法名
     * @param declaredField：对应的属性
     * @return set方法名
     */
    public String getSetMethodName(Field declaredField){
        String fieldName = declaredField.getName();
        String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        return methodName;
    }
}
