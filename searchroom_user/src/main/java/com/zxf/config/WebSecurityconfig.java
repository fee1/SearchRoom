package com.zxf.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.stereotype.Component;

@Component
public class WebSecurityconfig extends WebSecurityConfigurerAdapter {

    /**
     * 引入Bcrypt加密会导致拦截所有微服务的请求要求登录，使用此放行请求
     * https://www.cnblogs.com/zbjj-itblog/p/10730619.html
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /**
         * authorizeRequests所有security全注解配置实现的开端，表示开始说明需要的权限
         * 第一部分是拦截的路径，第二部分表示需要的权限
         * 任何请求，认证后才能访问                                               ？？？？？？？？
         * 固定写法，表示使csrf拦截失效，一种网络攻击技术，初了内部请求其它都会失效
         */
                http
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable();

            http.headers().frameOptions().sameOrigin();//释放iframe下的子窗口加载
    }
}
