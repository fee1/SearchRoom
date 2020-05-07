package com.zxf;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class BcryptTest {

    @Autowired
    BCryptPasswordEncoder encoder;

    /**
     * 强哈希密码测试
     */
    @Test
    public void encoder(){
        String s = "123";
        System.out.println(encoder.encode(s));
        System.out.println(encoder.encode(s));
    }

}
