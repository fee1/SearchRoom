package com.zxf.controller;

import com.zxf.service.SupportSubway;
import com.zxf.service.SupportSubwayStation;
import com.zxf.utils.ViewResultUtil;
import com.zxf.viewResult.ViewResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SupportSubwayController {

    @Autowired
    SupportSubway supportSubway;

    @Autowired
    SupportSubwayStation supportSubwayStation;

    /**
     * 获取城市对应的地铁线路
     * @param cityName
     * @return
     */
    @GetMapping("address/support/subway/line/{cityName}")
    @ResponseBody
    public ViewResult getSupportSubways(@PathVariable("cityName") String cityName){
        if (StringUtils.isBlank(cityName)){
            ViewResult result = new ViewResult();
            result.setMessage("城市名字不能为空");
            result.setCode(ViewResult.Status.BADREQUEST.getCode());
            return result;
        }
        ViewResult result = ViewResultUtil.getViewResult(supportSubway.getSupportSubways(cityName));
        return result;
    }

    /**
     * 获取对应地铁线路的站点
     * @param subwayLineId
     * @return
     */
    @GetMapping("address/support/subwayStation/{subwayLineId}")
    @ResponseBody
    public ViewResult getSupportSubwayStations(@PathVariable("subwayLineId") String subwayLineId){
        if (StringUtils.isBlank(subwayLineId)){
            ViewResult result = new ViewResult();
            result.setCode(ViewResult.Status.BADREQUEST.getCode());
            result.setMessage("地铁线路id不能为空");
            return result;
        }
        ViewResult result = ViewResultUtil.getViewResult(supportSubwayStation.getSupportSubwayStations(subwayLineId));
        return result;
    }

    /**
     * 地铁具体数据
     * @param subwayLineId
     * @return
     */
    @GetMapping("address/support/subway/lineDetail/{subwayLineId}")
    @ResponseBody
    public ViewResult getSupportSubway(@PathVariable("subwayLineId") Integer subwayLineId){
        if (StringUtils.isBlank(String.valueOf(subwayLineId))){
            ViewResult result = new ViewResult();
            result.setCode(ViewResult.Status.BADREQUEST.getCode());
            result.setMessage("地铁线路id不能为空");
            return result;
        }
        ViewResult result = ViewResultUtil.getViewResult(supportSubway.getSupportSubway(subwayLineId));
        return result;
    }

    /**
     * 获取具体地铁站点数据
     * @param subwayStationId
     * @return
     */
    @GetMapping("address/support/subwayStationDetail/{subwayStationId}")
    @ResponseBody
    public ViewResult getSupportSubwayStation(@PathVariable("subwayStationId") String subwayStationId){
        if (StringUtils.isBlank(subwayStationId)){
            ViewResult result = new ViewResult();
            result.setCode(ViewResult.Status.BADREQUEST.getCode());
            result.setMessage("地铁站点id不能为空");
            return result;
        }
        ViewResult result = ViewResultUtil.getViewResult(supportSubwayStation.getSupportSubwayStation(subwayStationId));
        return result;
    }

}
