package com.zxf.feignclient.impl;

import com.zxf.feignclient.SupportSubwayFeign;
import com.zxf.utils.ViewResultUtil;
import com.zxf.viewResult.ViewResult;
import org.springframework.stereotype.Component;

@Component
public class SupportSubwayFeignImpl implements SupportSubwayFeign {
    @Override
    public ViewResult getSupportSubways(String cityName) {
        return ViewResultUtil.getUnSuccess(ViewResult.Status.SERVERERROR.getCode(), "找不到服务");
    }

    @Override
    public ViewResult getSupportSubwayStations(String subwayLineId) {
        return ViewResultUtil.getUnSuccess(ViewResult.Status.SERVERERROR.getCode(), "找不到服务");
    }

    @Override
    public ViewResult getSupportSubway(Integer subwayLineId) {
        return ViewResultUtil.getUnSuccess(ViewResult.Status.SERVERERROR.getCode(), "找不到服务");
    }

    @Override
    public ViewResult getSupportSubwayStation(String subwayStationId) {
        return ViewResultUtil.getUnSuccess(ViewResult.Status.SERVERERROR.getCode(), "找不到服务");
    }

}
