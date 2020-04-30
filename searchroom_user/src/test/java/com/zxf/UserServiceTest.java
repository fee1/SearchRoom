package com.zxf;

import com.zxf.service.UserService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {

    @Autowired
    UserService userService;

//    @Test
//    public void testLogin(){
//        UserVo userVo = new UserVo();
//        userVo.setUsername("");
////        ServiceResult result = userService.login();
//    }

}
