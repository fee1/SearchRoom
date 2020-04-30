package com.zxf.aspectjTest;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Target {

    public Map pl(String name, int a){
        System.out.println(name);
        Map<String, Integer> map = new HashMap<>();
        map.put("code", 200);
        return map;
    }

}
