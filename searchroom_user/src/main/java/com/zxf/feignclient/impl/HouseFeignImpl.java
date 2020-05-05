package com.zxf.feignclient.impl;

import com.zxf.DTO.HouseDTO;
import com.zxf.DTO.HouseSearchDTO;
import com.zxf.VO.HouseSearchVo;
import com.zxf.feignclient.HouseFeign;
import com.zxf.VO.HouseVo;
import com.zxf.utils.ViewResultUtil;
import com.zxf.viewResult.ViewResult;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
public class HouseFeignImpl implements HouseFeign {
    @Override
    public ViewResult addHouse(HouseVo houseVo, String id) {
        return ViewResultUtil.getUnSuccess(ViewResult.Status.SERVERERROR.getCode(), "找不到服务");
    }

    @Override
    public HouseSearchDTO houses(HouseSearchVo houseSearchVo) {
        HouseSearchDTO responseVo = new HouseSearchDTO();
        responseVo.setCode(ViewResult.Status.SERVERERROR.getCode());
        responseVo.setMessage("找不到服务");
        return responseVo;
    }

    @Override
    public HouseDTO findHouse(String id) {
        return null;
    }

    @Override
    public ViewResult deletePhoto(String id) {
        return ViewResultUtil.getUnSuccess(ViewResult.Status.SERVERERROR.getCode(), "找不到服务");
    }

    @Override
    public ViewResult editHouse(HouseVo houseVo) {
        return ViewResultUtil.getUnSuccess(ViewResult.Status.SERVERERROR.getCode(), "找不到服务");
    }

    @Override
    public ViewResult addHouseTag(String id, String tag) {
        return ViewResultUtil.getUnSuccess(ViewResult.Status.SERVERERROR.getCode(), "找不到服务");
    }

    @Override
    public ViewResult deleteHouseTag(String id, String tag) {
        return ViewResultUtil.getUnSuccess(ViewResult.Status.SERVERERROR.getCode(), "找不到服务");
    }

    @Override
    public ViewResult setHouseCover(String coverId, String targetId) {
        return ViewResultUtil.getUnSuccess(ViewResult.Status.SERVERERROR.getCode(), "找不到服务");
    }

    @Override
    public ViewResult editHouseStatus(String houseId, String type) {
        return ViewResultUtil.getUnSuccess(ViewResult.Status.SERVERERROR.getCode(), "找不到服务");
    }

    @Override
    public ViewResult findHouseDTOS(String houseIds) {
        return ViewResultUtil.getUnSuccess(ViewResult.Status.SERVERERROR.getCode(), "找不到服务");
    }

    @Override
    public ViewResult addHouseWatchTimes(String houseId) {
        return ViewResultUtil.getUnSuccess(ViewResult.Status.SERVERERROR.getCode(), "找不到服务");
    }
}
