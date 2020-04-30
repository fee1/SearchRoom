package com.zxf.filter;

import com.zxf.utils.CusAccessObjectUtil;
import com.zxf.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpCookie;

import java.util.List;

@Component
public class MygateWayFilter implements GlobalFilter, Ordered {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 可以用于验证用户登录状态，设置某部分请求信息等。
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String uri = request.getURI().toString();
        //判断当前是什么类型请求GET请求直接放行
        if (StringUtils.equals(request.getMethod().toString(), "GET")){
            return chain.filter(exchange);
        }
        //不是GET，判断是不是登录
        if (uri.contains("login")){
            return chain.filter(exchange);
        }
        //如果不是GET请求，判断是否已经登录，token是否过期
        //获取cookie中的token令牌
        MultiValueMap<String, HttpCookie> multiValueMap = exchange.getRequest().getCookies();
        List<HttpCookie> cookies =  multiValueMap.get("token");
        if (cookies != null && cookies.size() != 0) {
            String token = cookies.get(0).getValue();
            //token为空说明未登录，不放行
            if (StringUtils.isBlank(token)){
                response.setStatusCode(HttpStatus.FORBIDDEN);
                return response.setComplete();
            }
            //token not null
            try {
                Claims claims = jwtUtil.parseJWT(token);
                String userId = claims.getId();
                Object userLoginIP = redisTemplate.opsForValue().get("token_"+userId);
                //判断是否被token盗用
                if (!StringUtils.equals(String.valueOf(userLoginIP), CusAccessObjectUtil.getIpAddress(request))){
                    response.setStatusCode(HttpStatus.FORBIDDEN);
                    return response.setComplete();
                }
                return chain.filter(exchange);
            }catch (Exception e){
                e.printStackTrace();
                response.setStatusCode(HttpStatus.FORBIDDEN);
                return response.setComplete();
            }
        }
        //没有cookie不放行
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return 0;
    }

}
