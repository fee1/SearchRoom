package com.zxf.service;

import com.zxf.serviceResult.ServiceResult;

public interface SupportSubwayStation {

    /**
     * 获取线路的站点
     * @param subwayLineId
     * @return
     */
    public ServiceResult getSupportSubwayStations(String subwayLineId);

    /**
     * 站点具体数据
     * @param subwayStationId
     * @return
     */
    ServiceResult getSupportSubwayStation(String subwayStationId);
}
