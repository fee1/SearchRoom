package com.zxf.service.impl;

import com.zxf.dao.SubwayMapper;
import com.zxf.DO.Subway;
import com.zxf.service.SupportSubway;
import com.zxf.serviceResult.ServiceResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupportSubwayImpl implements SupportSubway {

    @Autowired
    SubwayMapper subwayMapper;

    /**
     * 获取城市的地铁线路
     * @param cityName
     * @return
     */
    @Override
    public ServiceResult getSupportSubways(String cityName) {
        if (StringUtils.isBlank(cityName)){
            return ServiceResult.failure(ServiceResult.Status.PARAMERROR.getCode(), "城市名字不能为空");
        }
        List<Subway> subways = subwayMapper.selectByCityName(cityName);
        return ServiceResult.seccess("查询成功", subways);
    }

    /**
     * 地铁线路具体
     * @param subwayLineId
     * @return
     */
    @Override
    public ServiceResult getSupportSubway(Integer subwayLineId) {
        Subway subway = subwayMapper.selectByPrimaryKey(subwayLineId);
        return ServiceResult.seccess("查询成功", subway);
    }


}
