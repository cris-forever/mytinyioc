package com.cris.myspring.entity;

import com.cris.myspring.annotation.Autowired;
import com.cris.myspring.annotation.Component;
import com.cris.myspring.annotation.Qualifier;
import com.cris.myspring.annotation.Value;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Component("e")
public class Entity_1 {
    @Value("1")
    private Integer age;
    @Value("恋爱")
    private String name;
    @Autowired
    @Qualifier("f")
    private Fighting fight;
    @Value("1234454")
    private String phone;
    @Autowired
    @Qualifier("f")
    private Fighting fight2;
}
