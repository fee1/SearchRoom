package com.zxf.service;

import com.zxf.VO.MapSearchVo;
import com.zxf.VO.RentHouseSearchVo;
import com.zxf.result.PageResult;


public interface HouseSearchService {

    /**
     * 查找ES数据
     * @param searchVo
     * @return
     */
    PageResult searchHouses(RentHouseSearchVo searchVo);

    /**
     * 地图聚合查询 每个区域房源数量集合
     * @param cityEnName
     * @return
     */
    PageResult mapAggregation(String cityEnName);

    /**
     * 地图精确查询house数据
     * @param searchVo
     * @return
     */
    PageResult mapHouses(MapSearchVo searchVo);
}
