package com.zxf.service;

import com.github.pagehelper.Page;
import com.zxf.DTO.BaiduMapLocation;
import com.zxf.DTO.HouseDTO;
import com.zxf.VO.HouseSearchVo;
import com.zxf.VO.HouseVo;
import com.zxf.VO.RentHouseSearchVo;
import com.zxf.serviceResult.ServiceResult;
import com.zxf.viewResult.ViewResult;

import javax.servlet.http.HttpServletRequest;


public interface HouseService {

    /**
     * 添加房屋租赁信息
     * @param houseVo
     * @param id
     * @return
     */
    ServiceResult addHouse(HouseVo houseVo, String id);

    /**
     * admin房源信息列表展示
     * @param houseSearchVo
     * @return
     */
    public Page houses(HouseSearchVo houseSearchVo);

    /**
     * 根据房子id查找房子
     * @param id
     * @return
     */
    HouseDTO findHouse(String id, HttpServletRequest request);

    /**
     * 根据照片id数据库照片数据
     * @param id
     * @return
     */
    ServiceResult deletePhoto(String id);

    /**
     * 房屋信息编辑
     * @param houseVo
     * @return
     */
    ServiceResult editHouse(HouseVo houseVo);

    /**
     * 房屋标签添加
     * @param id
     * @return
     */
    ServiceResult addHouseTag(String id, String tag);

    /**
     * 房屋标签删除
     * @param id
     * @param tag
     * @return
     */
    ServiceResult deleteHouseTag(String id, String tag);

    /**
     * 房屋封面设置
     * @param coverId
     * @param targetId
     * @return
     */
    ServiceResult setHouseCover(String coverId, String targetId);

    /**
     * 改变房屋状态
     * @param houseId
     * @param type
     * @return
     */
    ServiceResult editHouseStatus(String houseId, String type);

    /**
     * user房源信息
     * @param searchVo
     * @return
     */
    Page houses(RentHouseSearchVo searchVo);

    /**
     * 当前小区几套在出租
     * @param cityEnName
     * @param regionEnName
     * @param district
     * @return
     */
    int getRentHouseSum(String cityEnName, String regionEnName, String district);

    /**
     * 查询房源数据
     * @param houseIds
     * @return
     */
    ServiceResult findHouseDTOS(String houseIds);

    /**
     * 增加房屋的被带看次数
     * @param houseId
     * @return
     */
    ServiceResult addHouseWatchTimes(String houseId);
}
