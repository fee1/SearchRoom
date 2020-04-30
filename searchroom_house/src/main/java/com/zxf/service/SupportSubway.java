package com.zxf.service;

import com.zxf.serviceResult.ServiceResult;

public interface SupportSubway {

    public ServiceResult getSupportSubways(String cityName);

    /**
     * 具体线路数据
     * @param subwayLineId
     * @return
     */
    ServiceResult getSupportSubway(Integer subwayLineId);

}
