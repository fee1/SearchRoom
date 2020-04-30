package com.zxf.interceptor;

import com.zxf.service.UserService;
import com.zxf.serviceResult.ServiceResult;
import com.zxf.utils.CookieUtils;
import com.zxf.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录判断拦截 废弃
 */
//@Component
public class MyInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    JwtUtil jwtUtil;

    @Value("${token_name}")
    private String TOKENNAME;

    @Value("${redirectURL}")
    private String redirectURL;

    @Autowired
    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = CookieUtils.getCookieValue(request, TOKENNAME);
        String requestURL = request.getRequestURI();
        //不存在token，未登录的情况下不放行，直接跳转到登录页面，跳转到登录页面以后在跳转回当前页面
        if (token == null){
            response.sendRedirect(redirectURL+"?url=/user-service"+requestURL);
            return false;
        }
        //执行判断是否存在盗用，或者过期
        ServiceResult serviceResult = userService.checkToken(token, request);
        if (serviceResult.getStatus() != ServiceResult.Status.SUCCESS.getCode()){
            response.sendRedirect(redirectURL+"?url=/user-service"+requestURL);
            return false;
        }
        return true;
    }
}
