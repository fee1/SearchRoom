package com.zxf.service;

import com.zxf.serviceResult.ServiceResult;

public interface SupportAddressService {

    /**
     * 获取支持的城市列表
     * @return
     */
    public ServiceResult getSupportCities();

    /**
     * 获取城市支持的区域列表
     * @return
     */
    public ServiceResult getSupportRegions(String cityName);

    /**
     * 城市具体信息
     * @param cityEnName
     * @return
     */
    ServiceResult getSupportCity(String cityEnName);

    /**
     * 具体的区域信息
     * @param cityEnName
     * @param regionEnName
     * @return
     */
    ServiceResult getSupportRegion(String cityEnName, String regionEnName);


}
