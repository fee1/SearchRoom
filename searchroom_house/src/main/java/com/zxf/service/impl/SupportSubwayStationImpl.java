package com.zxf.service.impl;

import com.zxf.dao.SubwayStationMapper;
import com.zxf.DO.SubwayStation;
import com.zxf.service.SupportSubwayStation;
import com.zxf.serviceResult.ServiceResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupportSubwayStationImpl implements SupportSubwayStation {

    @Autowired
    SubwayStationMapper subwayStationMapper;

    /**
     * 获取线路对应的站点
     * @param subwayLineId
     * @return
     */
    @Override
    public ServiceResult getSupportSubwayStations(String subwayLineId) {
        if (StringUtils.isBlank(subwayLineId)){
            return ServiceResult.failure(ServiceResult.Status.PARAMERROR.getCode(), "地铁线路id不能为空");
        }
        List<SubwayStation> subwayStations = subwayStationMapper.selectBySubwayId(subwayLineId);
        return ServiceResult.seccess("查询成功", subwayStations);
    }

    /**
     * 站点具体数据
     * @param subwayStationId
     * @return
     */
    @Override
    public ServiceResult getSupportSubwayStation(String subwayStationId) {
        SubwayStation subwayStation = subwayStationMapper.selectByPrimaryKey(Integer.valueOf(subwayStationId));
        return ServiceResult.seccess("查询成功", subwayStation);
    }

}
