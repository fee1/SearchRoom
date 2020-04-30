package com.zxf.aspectjTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TTarget {

    @Autowired
    Target target;

    @Test
    public void aj(){
        target.pl("xiaoming", 10);
    }

}
