package com.zxf.aspectjTest;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ApjectTset {

    @AfterReturning(value = "execution(public * com.zxf.aspectjTest.Target.pl(String, int )) && args(name, age)", returning = "result")
    public void get(Object result, String name, int age){
        System.out.println("返回参数："+JSON.toJSONString(result));
        System.out.println("传入参数："+name);
    }

}
