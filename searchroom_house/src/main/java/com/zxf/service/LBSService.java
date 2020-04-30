package com.zxf.service;

import com.zxf.DTO.BaiduMapLocation;
import com.zxf.serviceResult.ServiceResult;

/**
 * @author zxf
 * 云麻点服务
 */
public interface LBSService {


    /**
     * 获取地图地理编码经纬度
     * @param city
     * @param address
     * @return
     */
    BaiduMapLocation getBaiduMapLocation(String city, String address);
}
