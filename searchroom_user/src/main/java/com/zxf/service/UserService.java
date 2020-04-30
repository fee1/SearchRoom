package com.zxf.service;

import com.zxf.serviceResult.ServiceResult;
import com.zxf.VO.UserVo;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    /**
     * 用户登录
     * @param userVo
     * @return
     */
    public ServiceResult login(UserVo userVo, HttpServletRequest request);

    /**
     * 手机验证码登录方式
     * @param phone
     * @param smsCode
     * @param request
     * @return
     */
    public ServiceResult loginByPhone(String phone, String smsCode, HttpServletRequest request);

    /**
     * 用户名密码登录方式
     * @param userName
     * @param password
     * @param request
     * @return
     */
    public ServiceResult loginByUserName(String userName, String password, HttpServletRequest request);

    /**
     * 检查token是否过期
     * @param token
     * @return
     */
    public ServiceResult checkToken(String token, HttpServletRequest request);

    /**
     * 发送手机验证码
     * @return
     */
    public ServiceResult sendPhoneCode(String phoneNumber);

    /**
     * 根据id查找用户
     * @param adminId
     * @return
     */
    ServiceResult findUser(Integer adminId);

    /**
     * 修改用户信息
     * @param profile
     * @param value
     * @return
     */
    ServiceResult updateUserInfo(String profile, String value, HttpServletRequest request);

}
