package com.zxf.utils;

import com.zxf.utils.CookieUtils;
import com.zxf.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zxf
 */
@Component
public class UserUtil {

    @Autowired
    JwtUtil jwtUtil;

    @Value("${token_name}")
    String TOKENNAME;

    /**
     * 根据token获取用户信息
     * @return
     */
    public String getUserId(HttpServletRequest request){
        //获取token
        String token = CookieUtils.getCookieValue(request, TOKENNAME);
        String id = null;
        Claims claims = jwtUtil.parseJWT(token);
        //获取用户id
        id  = claims.getId();
        return id;
    }


}
