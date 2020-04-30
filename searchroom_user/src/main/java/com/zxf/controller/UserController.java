package com.zxf.controller;


import com.zxf.service.UserService;
import com.zxf.serviceResult.ServiceResult;
import com.zxf.utils.CookieUtils;
import com.zxf.utils.ViewResultUtil;
import com.zxf.viewResult.ViewResult;
import com.zxf.VO.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 普通用户
 * @author 朱晓峰
 */
@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Value("${token_name}")
    private String TOKENNAME;



    /**
     * 获取验证码
     * @param phoneNumber
     * @return
     */
    @GetMapping("sms/code/{phoneNumber}")
    @ResponseBody
    public ViewResult sendPhoneCode(@PathVariable("phoneNumber") String phoneNumber){
        ViewResult result = new ViewResult();
        //判断手机号是否为空
        if (StringUtils.isBlank(phoneNumber)){
            result.setMessage("手机号不能为空");
            result.setCode(ViewResult.Status.BADREQUEST.getCode());
            return result;
        }
        result = ViewResultUtil.getViewResult(userService.sendPhoneCode(phoneNumber));
        return result;
    }

    /**
     * 用户登录
     * @param userVo
     * @return
     */
    @PostMapping("user/login")
    public ModelAndView userLogin(UserVo userVo, Model model, ModelAndView modelAndView, HttpServletResponse response, HttpServletRequest request){
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


    /**
     * 登出，下线
     * @param request
     * @param response
     * @return
     */
    @GetMapping("user/loginOut")
    @ResponseBody
    public ViewResult loginOut(HttpServletRequest request, HttpServletResponse response){
        CookieUtils.deleteCookie(request, response, TOKENNAME);
        return ViewResult.SUCCESS();
    }

    /**
     * 检查token
     * @param
     * @return
     */
    @GetMapping("user/checkToken/{token}")
    @ResponseBody
    public ViewResult checkToken(@PathVariable("token") String token, ModelAndView modelAndView, Model model, HttpServletRequest request, HttpServletResponse response){
        ViewResult result = new ViewResult();
        if (StringUtils.isBlank(token)){
            result.setCode(ViewResult.Status.BADREQUEST.getCode());
            result.setMessage("不能传空参数");
            return result;
        }
        ServiceResult serviceResult = userService.checkToken(token, request);
        ViewResult viewResult = ViewResultUtil.getViewResult(serviceResult);
        viewResult.setMessage(serviceResult.getMessage());
        return viewResult;
    }

    /**
     * 查找用户
     * @param adminId
     * @return
     */
    @GetMapping("user/find")
    @ResponseBody
    public ViewResult findUser(Integer adminId){
        ViewResult result = new ViewResult();
        if (adminId == null || adminId <= 0){
            result.setCode(ViewResult.Status.BADREQUEST.getCode());
            result.setMessage("用户id为空");
            return result;
        }
        ServiceResult serviceResult = userService.findUser(adminId);
        return ViewResultUtil.getViewResult(serviceResult);
    }

    /**
     * 修改用户昵称、邮箱、密码
     * @param profile 修改类型
     * @param value 值
     * @return
     */
    @PostMapping("user/info")
    @ResponseBody
    public ViewResult updateUserInfo(@RequestParam("profile") String profile, @RequestParam("value") String value, HttpServletRequest request){
        ViewResult viewResult = new ViewResult();
        if (StringUtils.isBlank(profile)){
            viewResult.setMessage("修改的类型不能为空");
            viewResult.setCode(ViewResult.Status.BADREQUEST.getCode());
            return viewResult;
        }
        if (StringUtils.isBlank(value)){
            viewResult.setMessage("值不能为空");
            viewResult.setCode(ViewResult.Status.BADREQUEST.getCode());
            return viewResult;
        }
        ServiceResult serviceResult = userService.updateUserInfo(profile, value, request);
        return ViewResultUtil.getViewResult(serviceResult);
    }


}
