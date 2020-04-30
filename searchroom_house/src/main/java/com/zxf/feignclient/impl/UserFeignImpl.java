package com.zxf.feignclient.impl;

import com.zxf.feignclient.UserFeign;
import com.zxf.utils.ViewResultUtil;
import com.zxf.viewResult.ViewResult;
import org.springframework.stereotype.Component;

@Component
public class UserFeignImpl implements UserFeign {
    @Override
    public ViewResult findUser(Integer adminId) {
        return ViewResultUtil.getUnSuccess(ViewResult.Status.SERVERERROR.getCode(), "找不到服务");
    }

    @Override
    public ViewResult findSubscribeStatus(String houseId, String id1) {
        return ViewResultUtil.getUnSuccess(ViewResult.Status.SERVERERROR.getCode(), "找不到服务");
    }
}
