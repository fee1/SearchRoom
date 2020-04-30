package com.zxf.feignclient.impl;

import com.zxf.feignclient.SupportAddressFeign;
import com.zxf.utils.ViewResultUtil;
import com.zxf.viewResult.ViewResult;
import org.springframework.stereotype.Component;

@Component
public class SupportAddressFeignImpl implements SupportAddressFeign {
    @Override
    public ViewResult getSupportCities() {
        return ViewResultUtil.getUnSuccess(ViewResult.Status.SERVERERROR.getCode(), "找不到服务");
    }

    @Override
    public ViewResult getSupportCity(String cityEnName) {
        return ViewResultUtil.getUnSuccess(ViewResult.Status.SERVERERROR.getCode(), "找不到服务");
    }

    @Override
    public ViewResult getSupportRegions(String cityName) {
        return ViewResultUtil.getUnSuccess(ViewResult.Status.SERVERERROR.getCode(), "找不到服务");
    }

    @Override
    public ViewResult getSupportRegion(String cityEnName, String regionEnName) {
        return ViewResultUtil.getUnSuccess(ViewResult.Status.SERVERERROR.getCode(), "找不到服务");
    }

}
