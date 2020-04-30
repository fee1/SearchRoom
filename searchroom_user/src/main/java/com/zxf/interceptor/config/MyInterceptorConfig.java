package com.zxf.interceptor.config;

import com.zxf.interceptor.MyInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration 废弃
public class MyInterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private MyInterceptor myInterceptor;

    /**
     * 配置拦截的请求，与不拦截的请求
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(myInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/**/login", "/**/loginPage", "/**/static/**"); //这些请求不拦截

    }

}
