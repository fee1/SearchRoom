package com.zxf.service.impl;

import com.zxf.Constant;
import com.zxf.DO.HouseSubscribe;
import com.zxf.DTO.HouseDTO;
import com.zxf.DTO.UserDTO;
import com.zxf.dao.HouseSubscribeMapper;
import com.zxf.dao.UserMapper;
import com.zxf.DO.User;
import com.zxf.feignclient.HouseFeign;
import com.zxf.service.UserService;
import com.zxf.serviceResult.ServiceResult;
import com.zxf.utils.CookieUtils;
import com.zxf.utils.CusAccessObjectUtil;
import com.zxf.utils.JwtUtil;
import com.zxf.VO.UserVo;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * 用户服务
 * @author 朱晓峰
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 登录
     * @param userVo
     * @param request
     * @return
     */
    @Override
    public ServiceResult login(UserVo userVo, HttpServletRequest request) {
        ServiceResult result = null;
        //判断参数
        result = checkUserVoParam(userVo);
        if (result.getStatus() != ServiceResult.Status.SUCCESS.getCode()){
            return result;
        }
        //判断登录方式
        int way = checkUserLoginWay(userVo);
        if (way == -1){
            return loginByPhone(userVo.getTelephone(), userVo.getSmsCode(), request);
        }if (way == 1){
            return loginByUserName(userVo.getUsername(), userVo.getPassword(), request);
        }else{
            return ServiceResult.failure(ServiceResult.Status.EXCEPTION.getCode(), "未知方式登录");
        }
    }

    /**
     * 手机验证登录
     * @param phone
     * @param smsCode
     * @return
     */
    @Override
    public ServiceResult loginByPhone(String phone, String smsCode, HttpServletRequest request){
        //当前时间
        Date date = new Date();
        User user = userMapper.selectByPhone(phone);
        //从redis中获取验证码
        String checkCode = (String) redisTemplate.opsForValue().get("checkCode_"+phone);
        //如果没有这个用户就创建此用户
        if (user == null && StringUtils.equals(smsCode, checkCode)){
            User userCreater = new User();
            userCreater.setName(phone.substring(0,3)+"****"+phone.substring(7));
            userCreater.setPhoneNumber(phone);
            userCreater.setCreateTime(date);
            userCreater.setLastUpdateTime(date);
            userCreater.setLastLoginTime(date);
            userMapper.regiestUser(userCreater);
            return ServiceResult.seccess("登录成功", this.userIPAndSetRedis(userCreater, request));
        }else {
            if (StringUtils.equals(smsCode, checkCode)){
                user.setLastLoginTime(date);
                userMapper.updateByPrimaryKeySelective(user);
                return ServiceResult.seccess("登录成功", this.userIPAndSetRedis(user, request));
            }else{
                return ServiceResult.failure(ServiceResult.Status.PARAMERROR.getCode(), "验证码不正确");
            }
        }
    }

    public String userIPAndSetRedis(User user, HttpServletRequest request){
        //生成令牌
        String token = jwtUtil.createJWT(String.valueOf(user.getId()), user.getName(), "");
        //获取用户真实ip地址
        String userIP = CusAccessObjectUtil.getIpAddress(request);
        //token存入redis中  key=token_userID
        //可以通过此实现但设备登录与防止token盗用的情况
        redisTemplate.opsForValue().set("token_"+user.getId(), userIP , 60, TimeUnit.MINUTES);
        return token;
    }

    /**
     * 用户名登录
     * @param userName
     * @param password
     * @return
     */
    @Override
    public ServiceResult loginByUserName(String userName, String password, HttpServletRequest request){
        User user = userMapper.selectByUserName(userName);
        if (user == null){
            return ServiceResult.failure(ServiceResult.Status.PARAMERROR.getCode(), "用户名或密码不正确");
        }else{
            if (!bCryptPasswordEncoder.matches(password, user.getPassword())){
                return ServiceResult.failure(ServiceResult.Status.PARAMERROR.getCode(), "用户名或密码不正确");
            }
            user.setLastLoginTime(new Date());
            userMapper.updateByPrimaryKeySelective(user);
            return ServiceResult.seccess("登录成功", this.userIPAndSetRedis(user, request));
        }
    }

    /**
     * 检测token是否过期,或者存在被盗用的情况，单设备登录
     * @param token
     * @return
     */
    @Override
    public ServiceResult checkToken(String token,HttpServletRequest request) {
        try {
            //获取用户真实ip地址
            String userIP = CusAccessObjectUtil.getIpAddress(request);
            Claims claims = jwtUtil.parseJWT(token);
            Object value = redisTemplate.opsForValue().get("token_"+claims.getId());
            //判断是否token盗用
            if (!StringUtils.equals(String.valueOf(value), userIP)){
//                User user = userMapper.selectByPrimaryKey(Integer.valueOf(claims.getId()));
//                //短信通知用户他的登录地点方式可能存在风险
//                rabbitTemplate.convertAndSend("sms", user.getPhoneNumber());
                return ServiceResult.failure(ServiceResult.Status.EXCEPTION.getCode(), "token存在问题，请重新登录");
            }
            String username = claims.getSubject();
            return ServiceResult.seccess(username);
        }catch (Exception e){
            return ServiceResult.failure(ServiceResult.Status.EXCEPTION.getCode(), "登录已过期");
        }
    }

    /**
     * 手机发送验证码
     * @param phoneNumber
     * @return
     */
    @Override
    public ServiceResult sendPhoneCode(String phoneNumber) {
        //判断手机号是否正确
        if (!this.checkPhoneNumber(phoneNumber)){
            return ServiceResult.failure(ServiceResult.Status.PARAMERROR.getCode(), "手机号不正确");
        }
        //生成六位数字验证码
        String checkCode = RandomStringUtils.randomNumeric(6);
        //redis中保存一份用于校验   保存一分钟
        redisTemplate.opsForValue().set("checkCode_"+phoneNumber, checkCode, 15, TimeUnit.MINUTES);
        Map<String, String> map = new HashMap<>();
        map.put("phoneNumber", phoneNumber);
        map.put("checkCode", checkCode);
        //将手机号与验证码发送给rabbitmq队列
        rabbitTemplate.convertAndSend("searchroom_sms", map);
        return ServiceResult.seccess("成功发送验证码", checkCode);
    }

    /**
     * 根据id查找用户
     * @param adminId
     * @return
     */
    @Override
    public ServiceResult findUser(Integer adminId) {
        User user = userMapper.selectByPrimaryKey(adminId);
        if (user != null) {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            return ServiceResult.seccess("查询成功", userDTO);
        }else {
            return ServiceResult.failure(ServiceResult.Status.EXCEPTION.getCode(), "没有此用户");
        }
    }

    @Value("${token_name}")
    String TOKENNAME;

    /**
     * 根据token获取用户信息
     * @return
     */
    public String getUserId(HttpServletRequest request)throws Exception{
        //获取token
        String token = CookieUtils.getCookieValue(request, TOKENNAME);
        String id = null;
        Claims claims = jwtUtil.parseJWT(token);
        //获取用户id
        id  = claims.getId();
        return id;
    }

    /**
     * 修改用户信息
     * @param profile
     * @param value
     * @return
     */
    @Override
    public ServiceResult updateUserInfo(String profile, String value, HttpServletRequest request) {
        ServiceResult serviceResult = new ServiceResult();
        String id = null;
        try {
            id = this.getUserId(request);
        }catch (Exception e){
            return ServiceResult.failure(ServiceResult.Status.EXCEPTION.getCode(), "用户登录已过期");
        }
        if (StringUtils.isNotBlank(id)){
            User user = new User();
            user.setId(Integer.valueOf(id));
            if (StringUtils.equals(profile, "name")){
                user.setName(value);
            }else if (StringUtils.equals(profile, "email")){
                user.setEmail(value);
            }else if (StringUtils.equals(profile, "password")){
                user.setPassword(bCryptPasswordEncoder.encode(value));
            }
            int i = userMapper.updateByPrimaryKeySelective(user);
            if (i> 0){
                return ServiceResult.seccess("成功");
            }else {
                return ServiceResult.failure(ServiceResult.Status.EXCEPTION.getCode(), "修改失败");
            }
        }else {
            return ServiceResult.failure(ServiceResult.Status.EXCEPTION.getCode(), "没有此用户");
        }
    }


    /**
     * 判断用户传参是否完整正确
     * @return
     */
    public ServiceResult checkUserVoParam(UserVo userVo){
        //用户名与密码一同判断
        if (StringUtils.isNotBlank(userVo.getUsername()) && StringUtils.isNotBlank(userVo.getPassword())){
            return ServiceResult.seccess(ServiceResult.Status.SUCCESS.getMessage());
        }else if (StringUtils.isNotBlank(userVo.getTelephone()) && StringUtils.isNotBlank(userVo.getSmsCode())){

            if (userVo.getTelephone().length() != 11 || !this.checkPhoneNumber(userVo.getTelephone())) {
                return ServiceResult.failure(ServiceResult.Status.PARAMERROR.getCode(),"手机号不正确");
            }
            return ServiceResult.seccess(ServiceResult.Status.SUCCESS.getMessage());
        }else{
            return ServiceResult.failure(ServiceResult.Status.PARAMERROR.getCode(), "参数不正确");
        }
    }

    /**
     * 检查手机号规则是否正确
     * @param phoneNumber
     * @return
     */
    public boolean checkPhoneNumber(String phoneNumber){
        Pattern p = Pattern.compile(Constant.regex);
        return p.matcher(phoneNumber).matches();
    }

    /**
     * 检查用户登录的方式
     * @return
     * -1 ：手机号验证登录
     * 1 : 密码登录
     * 0 : 未知方式
     */
    public int checkUserLoginWay(UserVo userVo){
        if (StringUtils.isNotBlank(userVo.getTelephone())){
            return -1;
        }else if (StringUtils.isNotBlank(userVo.getUsername())){
            return 1;
        }else{
            return 0;
        }
    }
}
