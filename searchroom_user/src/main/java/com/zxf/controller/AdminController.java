package com.zxf.controller;

import com.zxf.VO.UserVo;
import com.zxf.service.UserService;
import com.zxf.service.UserSubscribeService;
import com.zxf.serviceResult.ServiceResult;
import com.zxf.utils.CookieUtils;
import com.zxf.utils.ViewResultUtil;
import com.zxf.viewResult.ViewResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *
 * @author zxf
 */
@Controller
@RequestMapping("admin")
public class AdminController {

    @Value("${token_name}")
    private String TOKENNAME;

    @Autowired
    UserService userService;

    @Autowired
    UserSubscribeService userSubscribeService;

    /**
     * 管理员登录
     * @param userVo
     * @return
     */
    @PostMapping("login")
    public ModelAndView adminLogin(UserVo userVo, Model model, ModelAndView modelAndView, HttpServletResponse response, HttpServletRequest request){
        ServiceResult serviceResult = userService.login(userVo, request);
        ViewResult result = ViewResultUtil.getViewResult(serviceResult);
        //设置token
        if (result.getCode() == ViewResult.Status.SUCCESS.getCode()){
            CookieUtils.setCookie(request, response, TOKENNAME, result.getData().toString());
        }
        model.addAttribute("result", result);
        modelAndView.addObject(model);
        return modelAndView;
    }


}
