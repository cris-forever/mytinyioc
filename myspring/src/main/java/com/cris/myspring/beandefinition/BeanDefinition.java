package com.cris.myspring.beandefinition;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class BeanDefinition {
    private Class beanClass;
    private String beanName;
}
