package com.zxf;

import com.zxf.utils.UserUtil;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PhotoTest {

    @Autowired
    UserUtil userUtil;

    /**
     * 删除照片测试
     */
//    @Test
//    public void deletePhoto(){
//        String fileName = "clouds1249278746553266176.png";
//        adminService.deletePhotoToClouds(fileName);
//    }

}
