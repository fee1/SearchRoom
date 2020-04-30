package com.zxf;

import com.zxf.dao.UserMapper;
import com.zxf.utils.UserUtil;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JdbcTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    UserMapper userMapper;

    @Autowired
    BCryptPasswordEncoder encoder;

    /**
     * 测试查找所有用户
     */
//    @Test
//    public void search(){
//        List<User> users = userMapper.selectAll();
//        System.out.println(users.size());
//    }

    /**
     * 数据库直接重置设置密码
     */
//    @Test
//    public void password(){
//        System.out.println(encoder.encode("123456"));
//    }

    @Autowired
    UserUtil userUtil;

    /**
     * 测试获取城市列表
     */
//    @Test
//    public void getSupportAddress(){
//        ServiceResult result = adminService.getSupportCities();
//        System.out.println(JSON.toJSONString(result));
//    }
}
