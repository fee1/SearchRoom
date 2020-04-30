package com.zxf.service;

import com.zxf.result.PageResult;
import com.zxf.serviceResult.ServiceResult;

import javax.servlet.http.HttpServletRequest;

public interface UserSubscribeService {

    /**
     * 将房源添加到待看清单
     * @param userId
     * @param houseId
     * @return
     */
    ServiceResult addSubscribe(String userId, String houseId);

    /**
     * 用户取消加入预约列表
     * @param userId
     * @param houseId
     * @return
     */
    ServiceResult deleteSubscribe(String userId, String houseId);

    /**
     * 获取预约状态
     * @param houseId
     * @param userId
     * @return
     */
    Integer getSubscribeStatus(String houseId, String userId);

    /**
     * 待看清单，预约记录，看房记录
     * @param status
     * @param start
     * @param size
     * @return
     */
    PageResult subscribeList(Integer status, Integer start, Integer size, HttpServletRequest request);

    /**
     * 房源预约
     * @param houseId
     * @param phone
     * @param orderTime
     * @return
     */
    ServiceResult reservation(String houseId, String phone, String orderTime, HttpServletRequest request);

    /**
     * 管理员预约请单
     * @param start
     * @param length
     * @return
     */
    PageResult adminSubscribeList(Integer start, Integer length, HttpServletRequest request);

    /**
     * 查看预约者信息
     * @param userId
     * @param houseId
     * @return
     */
    ServiceResult userSubscribeMsg(Integer userId, Integer houseId);

    /**
     * 看房完成
     * @param houseId
     * @param userId
     * @param request
     * @return
     */
    ServiceResult finshSubscribe(String houseId, String userId, HttpServletRequest request);
}
