package com.zxf.feignclient;

import com.zxf.feignclient.impl.UserFeignImpl;
import com.zxf.viewResult.ViewResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(value = "user-service", fallback = UserFeignImpl.class)
public interface UserFeign {

    /**
     * 查找用户
     * @param adminId
     * @return
     */
    @GetMapping("user/find")
    @ResponseBody
    ViewResult findUser(@RequestParam Integer adminId);

    /**
     * 获取用户查看当前房屋预约状态
     * @param houseId
     * @param userId
     * @return
     */
    @GetMapping("user/house/subscribeStatus")
    @ResponseBody
    ViewResult findSubscribeStatus(@RequestParam String houseId,@RequestParam String userId);
}
