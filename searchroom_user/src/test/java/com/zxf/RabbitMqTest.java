package com.zxf;

import com.alibaba.fastjson.JSON;
import com.zxf.service.UserService;
import com.zxf.serviceResult.ServiceResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RabbitMqTest {

    @Autowired
    UserService userService;

    /**
     * rabbitmq测试
     */
//    @Test
//    public void testSms(){
//        ServiceResult result = userService.sendPhoneCode("17687374997");
//        System.out.println(JSON.toJSONString(result));
//    }

}
