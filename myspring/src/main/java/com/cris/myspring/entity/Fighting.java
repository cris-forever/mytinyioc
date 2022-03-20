package com.cris.myspring.entity;

import com.cris.myspring.annotation.Component;
import com.cris.myspring.annotation.Value;
import lombok.Data;

@Data
@Component("f")
public class Fighting {
    @Value("一定要发狠读书！，然后赚钱找女朋友！")
    private String s;
    @Value("6.66")
    private Float f;
    @Value("一定能找到实习的！")
    private String s2;
}
