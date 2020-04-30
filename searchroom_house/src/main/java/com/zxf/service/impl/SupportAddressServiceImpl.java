package com.zxf.service.impl;

import com.zxf.dao.SupportAddressMapper;
import com.zxf.DO.SupportAddress;
import com.zxf.service.SupportAddressService;
import com.zxf.serviceResult.ServiceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupportAddressServiceImpl implements SupportAddressService {

    @Autowired
    SupportAddressMapper supportAddressMapper;

    /**
     * 获取支持的城市列表
     * @return
     */
    @Override
    public ServiceResult getSupportCities() {
        List<SupportAddress> supportAddresses = supportAddressMapper.selectSupportCities();
        return ServiceResult.seccess("查询成功", supportAddresses);
    }

    /**
     * 获取城市支持的区域列表
     * @return
     */
    @Override
    public ServiceResult getSupportRegions(String cityName) {
        List<SupportAddress> supportAddresses = supportAddressMapper.selectSupportRegionsBycityName(cityName);
        return ServiceResult.seccess("查询成功", supportAddresses);
    }

    /**
     * 具体城市信息
     * @param cityEnName
     * @return
     */
    @Override
    public ServiceResult getSupportCity(String cityEnName) {
        SupportAddress supportAddress = supportAddressMapper.selectSupportCity(cityEnName);
        return ServiceResult.seccess("查询成功", supportAddress);
    }

    /**
     * 具体区域信息
     * @param cityEnName
     * @param regionEnName
     * @return
     */
    @Override
    public ServiceResult getSupportRegion(String cityEnName, String regionEnName) {
        SupportAddress supportAddress = supportAddressMapper.selectSupportRegion(cityEnName, regionEnName);
        return ServiceResult.seccess("查询成功", supportAddress);
    }

}
