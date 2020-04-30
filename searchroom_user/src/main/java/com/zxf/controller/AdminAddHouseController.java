package com.zxf.controller;

import com.alibaba.fastjson.JSON;
import com.zxf.VO.HouseVo;
import com.zxf.feignclient.HouseFeign;
import com.zxf.feignclient.SupportAddressFeign;
import com.zxf.feignclient.SupportSubwayFeign;
import com.zxf.service.AdminAddHouseService;
import com.zxf.service.UserService;
import com.zxf.utils.ViewResultUtil;
import com.zxf.viewResult.ViewResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 房屋添加
 * @author zxf
 */
@Controller
@RequestMapping("admin")
public class AdminAddHouseController {

    @Autowired
    AdminAddHouseService adminAddHouseService;

    @Autowired
    UserService userService;

    @Autowired
    HouseFeign houseFeign;

    @Autowired
    SupportAddressFeign supportAddressFeign;

    @Autowired
    SupportSubwayFeign supportSubwayFeign;

    /**
     * 获取支持的城市列表
     * @return
     */
    @GetMapping("address/support/cities")
    @ResponseBody
    public ViewResult getSupportCities(){
        ViewResult result = supportAddressFeign.getSupportCities();
        return result;
    }

    /**
     * 获取支持对应的城市支持的区域表
     */
    @GetMapping("address/support/regions/{cityName}")
    @ResponseBody
    public ViewResult getSupportRegions(@PathVariable("cityName") String cityName){
        if (StringUtils.isBlank(cityName)){
            ViewResult result = new ViewResult();
            result.setCode(ViewResult.Status.BADREQUEST.getCode());
            result.setMessage("城市名字不能为空");
            return result;
        }
        ViewResult result = supportAddressFeign.getSupportRegions(cityName);
        return result;
    }

    /**
     * 获取支持的出行地铁
     * @return
     */
    @GetMapping("address/support/subway/line/{cityName}")
    @ResponseBody
    public ViewResult getSupportSubway(@PathVariable("cityName") String cityName){
        if (StringUtils.isBlank(cityName)){
            ViewResult result = new ViewResult();
            result.setMessage("城市名字不能为空");
            result.setCode(ViewResult.Status.BADREQUEST.getCode());
            return result;
        }
        ViewResult result = supportSubwayFeign.getSupportSubways(cityName);
        return result;
    }

    /**
     * 获取对应地铁线路的站点
     * @param subwayLineId
     * @return
     */
    @GetMapping("address/support/subwayStation/{subwayLineId}")
    @ResponseBody
    public ViewResult getSupportSubwayStation(@PathVariable("subwayLineId") String subwayLineId){
        if (StringUtils.isBlank(subwayLineId)){
            ViewResult result = new ViewResult();
            result.setCode(ViewResult.Status.BADREQUEST.getCode());
            result.setMessage("地铁线路id不能为空");
            return result;
        }
        ViewResult result = supportSubwayFeign.getSupportSubwayStations(subwayLineId);
        return result;
    }

    /**
     * 房屋照片上传
     * @param file
     * @return
     */
    @PostMapping("add/upload/photo")
    @ResponseBody
    public ViewResult uploadPhoto(@RequestParam("file") MultipartFile file){
        if (file.isEmpty()) {
            return ViewResultUtil.getUnSuccess(ViewResult.Status.BADREQUEST.getCode(), "不能传空文件");
        }
        //上传到云存储
        ViewResult result = ViewResultUtil.getViewResult(adminAddHouseService.uploadPhotoToClouds(file));
        //上传到本地(在上传云存储失败的情况下，开启本地存储)
        if (result.getCode() != ViewResult.Status.SUCCESS.getCode()) {
            result = ViewResultUtil.getViewResult(adminAddHouseService.uploadPhotoInLocal(file));
        }
        return result;
    }


    /**
     * 添加租赁信息
     * @param houseVo
     * @param request
     * @return
     */
    @PostMapping("add/house")
    @ResponseBody
    public ViewResult addHouse(HouseVo houseVo, HttpServletRequest request){
        ViewResult result = new ViewResult();
        if (houseVo == null){
            result.setMessage(ViewResult.Status.BADREQUEST.getMessage());
            result.setCode(ViewResult.Status.BADREQUEST.getCode());
            return result;
        }
        //
        result = adminAddHouseService.addHouse(houseVo, request);
        return result;
    }



}
