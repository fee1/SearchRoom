package com.zxf.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserPageController {

    /**
     * 普通用户登录页
     * @return
     */
    @GetMapping("user/loginPage")
    public String userLoginPage(){
        return "user/login";
    }

    /**
     * 普通用户中心
     * @return
     */
    @GetMapping("user/center")
    public String userCenter(){
        return "user/center";
    }
}
