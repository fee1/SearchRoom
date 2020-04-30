package com.zxf.controller;

import com.zxf.DTO.HouseSearchDTO;
import com.zxf.result.PageResult;
import com.zxf.utils.UserUtil;
import com.zxf.service.UserSubscribeService;
import com.zxf.serviceResult.ServiceResult;
import com.zxf.utils.ViewResultUtil;
import com.zxf.viewResult.ViewResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zxf
 * 预约等操作
 */
@Controller
public class UserSubscribeController {

    @Autowired
    UserUtil userUtil;

    @Autowired
    UserSubscribeService userSubscribeService;

    /**
     * 用户将房源添加到待预约列表
     * @return
     */
    @PostMapping("user/house/subscribe")
    @ResponseBody
    public ViewResult addSubscribe(HttpServletRequest request, @RequestParam("houseId")String houseId){
        if (StringUtils.isBlank(houseId)){
            return ViewResultUtil.getUnSuccess(ViewResult.Status.BADREQUEST.getCode(), "房屋Id缺失");
        }
        String userId = userUtil.getUserId(request);
        ServiceResult serviceResult = userSubscribeService.addSubscribe(userId, houseId);
        return ViewResultUtil.getViewResult(serviceResult);
    }

    /**
     * 用户取消加入预约列表
     * @param request
     * @param houseId
     * @return
     */
    @DeleteMapping("user/house/subscribe/{houseId}")
    @ResponseBody
    public ViewResult deleteSubscribe(HttpServletRequest request, @PathVariable String houseId){
        if (StringUtils.isBlank(houseId)){
            return ViewResultUtil.getUnSuccess(ViewResult.Status.BADREQUEST.getCode(), "房屋Id缺失");
        }
        String userId = userUtil.getUserId(request);
        ServiceResult serviceResult = userSubscribeService.deleteSubscribe(userId, houseId);
        return ViewResultUtil.getViewResult(serviceResult);
    }

    /**
     * 查询房子预约状态
     * @param houseId
     * @param userId
     * @return
     */
    @GetMapping("user/house/subscribeStatus")
    @ResponseBody
    public ViewResult getSubscribeStatus(String houseId, String userId){
        if (StringUtils.isBlank(houseId) || StringUtils.isBlank(userId)){
            return ViewResultUtil.getUnSuccess(ViewResult.Status.BADREQUEST.getCode(), ViewResult.Status.BADREQUEST.getMessage());
        }
        return ViewResult.SUCCESS(userSubscribeService.getSubscribeStatus(houseId, userId));
    }

    /**
     * 待看清单，预约记录，看房记录
     * @param status
     * @param start
     * @param size
     * @return
     */
    @GetMapping("user/house/subscribe/list")
    @ResponseBody
    public PageResult subscribeList(@RequestParam("status") Integer status, @RequestParam(value = "start", defaultValue = "1") Integer start,
                                    @RequestParam(value = "size", defaultValue = "5") Integer size, HttpServletRequest request){
        if (status == null){
            return new PageResult(false);
        }
        PageResult pageResult = userSubscribeService.subscribeList(status, start, size, request);
        return pageResult;
    }

    /**
     * 预约房源
     * @param houseId
     * @param phone
     * @param date
     * @return
     */
    @PostMapping("user/house/subscribe/date")
    @ResponseBody
    public ViewResult reservation(@RequestParam("houseId") String houseId, @RequestParam("telephone")String phone, @RequestParam("orderTime")String orderTime, HttpServletRequest request){
        if (StringUtils.isBlank(houseId)){
            return ViewResultUtil.getUnSuccess(ViewResult.Status.BADREQUEST.getCode(), "房屋ID缺失");
        }
        if (StringUtils.isBlank(phone)){
            return ViewResultUtil.getUnSuccess(ViewResult.Status.BADREQUEST.getCode(), "手机号不能为空");
        }
        if (StringUtils.isBlank(orderTime)) {
            return ViewResultUtil.getUnSuccess(ViewResult.Status.BADREQUEST.getCode(), "预约时间不能为空");
        }
        ServiceResult serviceResult = userSubscribeService.reservation(houseId, phone, orderTime, request);
        return ViewResultUtil.getViewResult(serviceResult);
    }

    @GetMapping("admin/house/subscribe/list")
    @ResponseBody
    public HouseSearchDTO adminSubscribeList(Integer draw, Integer start, Integer length, HttpServletRequest request){
        PageResult pageResult = userSubscribeService.adminSubscribeList(start, length, request);
        HouseSearchDTO houseSearchDTO = new HouseSearchDTO();
        houseSearchDTO.setDraw(draw);
        houseSearchDTO.setRecordsTotal(pageResult.getTotal() == null ? 0 : pageResult.getTotal());
        houseSearchDTO.setRecordsFiltered(pageResult.getTotal() == null ? 0 :pageResult.getTotal());
        houseSearchDTO.setData(pageResult.getResult());
        return houseSearchDTO;
    }

    /**
     * 查看预约者信息
     */
    @GetMapping("admin/house/user/subscribe/detail/{userId}/{houseId}")
    @ResponseBody
    public ViewResult userSubscribeMsg(@PathVariable Integer userId,@PathVariable Integer houseId){
        if (userId == null || userId <= 0){
            return ViewResultUtil.getUnSuccess(ViewResult.Status.BADREQUEST.getCode(), "参数错误");
        }
        if (houseId == null || houseId <= 0){
            return ViewResultUtil.getUnSuccess(ViewResult.Status.BADREQUEST.getCode(), "参数错误");
        }
        ServiceResult serviceResult = userSubscribeService.userSubscribeMsg(userId, houseId);
        return ViewResultUtil.getViewResult(serviceResult);
    }

    /**
     * 房源管理者带看完成
     */
    @PostMapping("admin/house/finish/subscribe")
    @ResponseBody
    public ViewResult finshSubscribe(@RequestParam("houseId") String houseId,@RequestParam("userId")String userId, HttpServletRequest request){
        if (StringUtils.isBlank(houseId)){
            return ViewResultUtil.getUnSuccess(ViewResult.Status.BADREQUEST.getCode(), "房屋ID为空");
        }
        if (StringUtils.isBlank(userId)){
            return ViewResultUtil.getUnSuccess(ViewResult.Status.BADREQUEST.getCode(), "用户ID为空");
        }
        ServiceResult serviceResult = userSubscribeService.finshSubscribe(houseId, userId, request);
        return ViewResultUtil.getViewResult(serviceResult);
    }

}
