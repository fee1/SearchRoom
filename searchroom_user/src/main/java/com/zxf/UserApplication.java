package com.zxf;

import com.zxf.utils.IdWorker;
import com.zxf.utils.AliyunFileUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
 * 用户微服务
 * @author zxf
 */
@SpringBootApplication          //(exclude = SecurityAutoConfiguration.class)//关闭http登录验证
@EnableDiscoveryClient         //非一定需要才能注册到nacos
@EnableFeignClients          //表示打开Feign功能
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

    /**
     * 密码加密工具
     * @return
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 云存储上传下载工具
     * @return
     */
    @Bean
    public AliyunFileUtil uploadAndDownloadFileUtil(){
        return new AliyunFileUtil();
    }

    /**
     * id工具生成算法工具
     * @return
     */
    @Bean
    public IdWorker idWorker(){
        return new IdWorker();
    }

}
