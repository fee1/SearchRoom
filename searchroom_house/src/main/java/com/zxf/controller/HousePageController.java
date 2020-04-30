package com.zxf.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.zxf.DTO.HouseDTO;
import com.zxf.DTO.RentValueBlockDTO;
import com.zxf.VO.RentHouseSearchVo;
import com.zxf.feignclient.UserFeign;
import com.zxf.service.HouseService;
import com.zxf.service.SupportAddressService;
import com.zxf.serviceResult.ServiceResult;
import com.zxf.viewResult.ViewResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HousePageController {

    @Autowired
    SupportAddressService addressService;

    @Autowired
    HouseService houseService;

    @Autowired
    UserFeign userFeign;

    /**
     * 区域出租房屋搜索列表
     * @return
     */
    @GetMapping("rent/house")
    public ModelAndView rentHouseList(RentHouseSearchVo searchVo, ModelAndView modelAndView){
        if (StringUtils.isBlank(searchVo.getCityEnName())){
            modelAndView.setViewName("/home-service/index");
            modelAndView.addObject("msg", "必须选择城市");
            return modelAndView;
        }
        //当前查找体信息
        modelAndView.addObject("searchBody", searchVo);

        //当前城市区域信息
        ServiceResult serviceResult = addressService.getSupportCity(searchVo.getCityEnName());
        modelAndView.addObject("currentCity", serviceResult.getData());

        //当前城市支持区域信息
        serviceResult = addressService.getSupportRegions(searchVo.getCityEnName());
        modelAndView.addObject("regions", serviceResult.getData());

        //当前城市的房源信息
        Page page = houseService.houses(searchVo);
        modelAndView.addObject("houses", page.getResult());

        //总数
        modelAndView.addObject("total", page.getTotal());

        //价格区间
        modelAndView.addObject("priceBlocks", RentValueBlockDTO.PRICE_BLOCK);


        //面积区间
        modelAndView.addObject("areaBlocks", RentValueBlockDTO.AREA_BLOCK);
        //当前价格区间
        modelAndView.addObject("currentPriceBlock", RentValueBlockDTO.matchPrice(searchVo.getPriceBlock()));
        //当前面积区间
        modelAndView.addObject("currentAreaBlock",RentValueBlockDTO.matchArea(searchVo.getAreaBlock()));

        modelAndView.setViewName("rent-list");
        return modelAndView;
    }

    /**
     * 房子详情页展示
     * @return
     */
    @GetMapping("rent/house/show/{houseId}")
    public ModelAndView showHouseDetail(@PathVariable String houseId, ModelAndView modelAndView, HttpServletRequest request){
        if (StringUtils.isBlank(houseId) || Integer.valueOf(houseId) < 0){
            modelAndView.setViewName("404");
            modelAndView.addObject("msg", "房子Id必须");
            return modelAndView;
        }
        HouseDTO houseDTO = houseService.findHouse(houseId, request);
        //当前房源具体信息
        modelAndView.addObject("house", houseDTO);
        ServiceResult serviceResult = addressService.getSupportCity(houseDTO.getCityEnName());
        //当前城市
        modelAndView.addObject("city", serviceResult.getData());
        //当前区域
        serviceResult = addressService.getSupportRegion(houseDTO.getCityEnName(), houseDTO.getRegionEnName());
        modelAndView.addObject("region", serviceResult.getData());
        //发布者信息
        ViewResult viewResult = userFeign.findUser(houseDTO.getAdminId());
        modelAndView.addObject("agent", viewResult.getData());
        //当前区域出租信息
        int rentHouseSum = houseService.getRentHouseSum(houseDTO.getCityEnName(), houseDTO.getRegionEnName(), houseDTO.getDistrict());
        modelAndView.addObject("houseCountInDistrict", rentHouseSum);
        modelAndView.setViewName("house-detail");
        return modelAndView;
    }



}
