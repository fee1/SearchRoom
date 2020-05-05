package com.zxf.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zxf.DO.HouseSubscribe;
import com.zxf.DO.User;
import com.zxf.DTO.HouseDTO;
import com.zxf.dao.HouseSubscribeMapper;
import com.zxf.dao.UserMapper;
import com.zxf.feignclient.HouseFeign;
import com.zxf.result.PageResult;
import com.zxf.utils.UserUtil;
import com.zxf.service.UserService;
import com.zxf.service.UserSubscribeService;
import com.zxf.serviceResult.ServiceResult;
import com.zxf.viewResult.ViewResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class UserSubscribeServiceImpl implements UserSubscribeService {

    @Autowired
    HouseFeign houseFeign;

    @Autowired
    HouseSubscribeMapper houseSubscribeMapper;

    @Autowired
    UserUtil userUtil;

    @Autowired
    UserService userService;

    @Autowired
    UserMapper userMapper;

    /**
     * 将房源添加到待预约列表中
     * @param userId
     * @param houseId
     * @return
     */
    @Override
    public ServiceResult addSubscribe(String userId, String houseId) {
        HouseSubscribe subscribe = new HouseSubscribe();
        subscribe.setHouseId(Integer.valueOf(houseId));
        subscribe.setUserId(Integer.valueOf(userId));
        HouseDTO houseDTO = houseFeign.findHouse(houseId);
        System.out.println(houseDTO);
        if (houseDTO == null){
            return ServiceResult.failure(ServiceResult.Status.EXCEPTION.getCode(), "获取房屋信息失败");
        }
        subscribe.setAdminId(houseDTO.getAdminId());
        subscribe.setStatus(1);
        Date date = new Date();
        subscribe.setCreateTime(date);
        subscribe.setLastUpdateTime(date);
        int i = houseSubscribeMapper.insertSelective(subscribe);
        if (i <= 0){
            return ServiceResult.failure(ServiceResult.Status.EXCEPTION.getCode(), "数据添加失败");
        }
        return ServiceResult.seccess("成功");
    }

    /**
     * 用户取消加入预约列表
     * @param userId
     * @param houseId
     * @return
     */
    @Override
    public ServiceResult deleteSubscribe(String userId, String houseId) {
        int i = houseSubscribeMapper.deleteSubscribe(userId, houseId);
        if (i > 0) {
            return ServiceResult.seccess("成功");
        }else {
            return ServiceResult.failure(ServiceResult.Status.EXCEPTION.getCode(), "取消预约失败");
        }
    }

    /**
     * 获取房屋预约状态
     * @param houseId
     * @param userId
     * @return
     */
    @Override
    public Integer getSubscribeStatus(String houseId, String userId) {
        HouseSubscribe houseSubscribe = houseSubscribeMapper.getSubscribe(houseId, userId);
        if (houseSubscribe != null) {
            return houseSubscribe.getStatus();
        }else{
            return 0;
        }
    }

    /**
     * 待看清单，预约记录，看房记录
     * @param status
     * @param start
     * @param size
     * @return
     */
    @Override
    public PageResult subscribeList(Integer status, Integer start, Integer size, HttpServletRequest request) {
        String userId = userUtil.getUserId(request);
        Page<HouseSubscribe> page = PageHelper.startPage(start/size+1, size, true);
        houseSubscribeMapper.subscribeList(userId, status);
        List<HouseSubscribe> houseSubscribes = page.getResult();
        if (houseSubscribes.size() <= 0){
            return new PageResult();
        }
        StringBuffer houseIds = new StringBuffer();
        for (HouseSubscribe houseSubscribe : houseSubscribes){
            houseIds.append(houseSubscribe.getHouseId().toString()+",");
        }
        ViewResult result = houseFeign.findHouseDTOS(houseIds.toString().substring(0, houseIds.length()-1));
        if (result.getCode() != ViewResult.Status.SUCCESS.getCode()) {
            return new PageResult(false);
        }
        List<HouseDTO> houseDTOS = JSON.parseArray(JSON.toJSONString(result.getData()), HouseDTO.class);

        List<Pair<HouseDTO, HouseSubscribe>> pairs = new ArrayList<>();
        if (houseSubscribes.size() != 0) {
            for (HouseSubscribe subscribe : houseSubscribes) {
                for (HouseDTO houseDTO : houseDTOS) {
                    if (houseDTO.getId().equals(subscribe.getHouseId())) {
                        pairs.add(Pair.of(houseDTO, subscribe));
                        continue;
                    }
                }
            }
        }else {
            return new PageResult(false);
        }

        PageResult pageResult = new PageResult();
        pageResult.setTotal(Integer.valueOf(Long.toString(page.getTotal())));
        pageResult.setResult(pairs);
        pageResult.setMore(Integer.valueOf(Long.toString(page.getTotal())) > (start/size+1)*size);
        return pageResult;
    }

    /**
     * 房源预约
     * @param houseId
     * @param phone
     * @param orderTime
     * @return
     */
    @Override
    public ServiceResult reservation(String houseId, String phone, String orderTime, HttpServletRequest request) {
        //获取用户id
        String userId = userUtil.getUserId(request);
        String updateTime = null;
        try {
            updateTime = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
        }catch (Exception e){
            e.printStackTrace();
            return ServiceResult.failure(ServiceResult.Status.EXCEPTION.getCode(), "日期转换异常");
        }
        int i = houseSubscribeMapper.reservation(userId, houseId, phone, orderTime, updateTime);
        if (i < 0){
            return ServiceResult.failure(ServiceResult.Status.EXCEPTION.getCode(), "预约失败");
        }
        return ServiceResult.seccess("预约成功");
    }

    /**
     * 管理员预约请单
     * @param start
     * @param length
     * @return
     */
    @Override
    public PageResult adminSubscribeList(Integer start, Integer length, HttpServletRequest request) {
        String adminId = userUtil.getUserId(request);
        Page<HouseSubscribe> page = PageHelper.startPage(start / length+1, length, true);
        houseSubscribeMapper.adminSubscribeList(adminId);
        List<HouseSubscribe> houseSubscribes = page.getResult();
        if (houseSubscribes.size() <= 0){
            return new PageResult(false);
        }
        StringBuffer houseIds = new StringBuffer();
        for (HouseSubscribe houseSubscribe : houseSubscribes){
            houseIds.append(houseSubscribe.getHouseId()+",");
        }
        ViewResult viewResult = houseFeign.findHouseDTOS(houseIds.toString().substring(0, houseIds.length()-1));
        if (viewResult.getCode() != ViewResult.Status.SUCCESS.getCode()){
            return new PageResult(false);
        }
        List<HouseDTO> houseDTOS = JSON.parseArray(JSON.toJSONString(viewResult.getData()), HouseDTO.class);

        List<Pair<HouseDTO, HouseSubscribe>> pairs = new ArrayList<>();
        for (HouseSubscribe houseSubscribe : houseSubscribes){
            for (HouseDTO houseDTO: houseDTOS){
                if (houseSubscribe.getHouseId().equals(houseDTO.getId())){
                    pairs.add(Pair.of(houseDTO, houseSubscribe));
                }
            }
        }

        PageResult pageResult = new PageResult();
        pageResult.setMore(Integer.valueOf(Long.toString(page.getTotal())) > (start / length+1)*length);
        pageResult.setResult(pairs);
        pageResult.setTotal(Integer.valueOf(Long.toString(page.getTotal())));
        return pageResult;
    }

    /**
     * 预约者信息
     * @param userId
     * @param houseId
     * @return
     */
    @Override
    public ServiceResult userSubscribeMsg(Integer userId, Integer houseId) {
        User user = userMapper.selectByPrimaryKey(userId);
        Map<String, String> data = new HashMap<>();
        data.put("name", StringUtils.isBlank(user.getName()) ? "": user.getName());
        HouseSubscribe subscribe = houseSubscribeMapper.getSubscribe(houseId.toString(), userId.toString());
        if (subscribe != null) {
            data.put("phoneNumber", subscribe.getTelephone());
        }
        return ServiceResult.seccess("成功", data);
    }

    /**
     * 带看完成
     * @param houseId
     * @param userId
     * @param request
     * @return
     */
    @Override
    public ServiceResult finshSubscribe(String houseId, String userId, HttpServletRequest request) {
        Date updateTime = new Date();
        //增加房屋被带看次数
        ViewResult viewResult = houseFeign.addHouseWatchTimes(houseId);
        if (viewResult.getCode() == ViewResult.Status.SUCCESS.getCode()) {
            int i = houseSubscribeMapper.finshSubscribe(houseId, userId, updateTime);
            if (i <= 0) {
                return ServiceResult.failure(ServiceResult.Status.EXCEPTION.getCode(), "数据更新失败");
            }
        }else {
            return ServiceResult.failure(ServiceResult.Status.EXCEPTION.getCode(), "带看次数未更新");
        }
        return ServiceResult.seccess("成功");
    }
}
