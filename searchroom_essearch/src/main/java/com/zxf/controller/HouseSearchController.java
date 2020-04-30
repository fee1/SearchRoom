package com.zxf.controller;

import com.zxf.DTO.RentValueBlockDTO;
import com.zxf.VO.MapSearchVo;
import com.zxf.VO.RentHouseSearchVo;
import com.zxf.feignclient.SupportAddressFeign;
import com.zxf.result.PageResult;
import com.zxf.service.HouseSearchService;
import com.zxf.utils.ViewResultUtil;
import com.zxf.viewResult.ViewResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HouseSearchController {

    @Autowired
    HouseSearchService houseSearchService;

    @Autowired
    SupportAddressFeign supportAddressFeign;

    @GetMapping("rent/house")
    public ModelAndView searchHouses(RentHouseSearchVo searchVo, ModelAndView modelAndView){
        if (StringUtils.isBlank(searchVo.getCityEnName())){
            modelAndView.setViewName("/home-service/index");
            modelAndView.addObject("msg", "必须选择城市");
            return modelAndView;
        }
        //当前查找体信息
        modelAndView.addObject("searchBody", searchVo);

        //当前城市区域信息
        ViewResult viewResult = supportAddressFeign.getSupportCity(searchVo.getCityEnName());
        modelAndView.addObject("currentCity", viewResult.getData());

        //当前城市支持区域信息
        viewResult = supportAddressFeign.getSupportRegions(searchVo.getCityEnName());
        modelAndView.addObject("regions", viewResult.getData());

        //当前城市的房源信息
        PageResult pageResult = houseSearchService.searchHouses(searchVo);
        modelAndView.addObject("houses", pageResult.getResult());

        //总数
        modelAndView.addObject("total", pageResult.getTotal());

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
     * 地图map页
     * @param modelAndView
     * @param cityEnName
     * @return
     */
    @GetMapping("rent/house/map")
    public ModelAndView mapPage(ModelAndView modelAndView,String cityEnName){
        if (StringUtils.isBlank(cityEnName)){
            modelAndView.setViewName("/home-service/index");
            modelAndView.addObject("msg", "必须选择城市");
            return modelAndView;
        }

        //当前城市具体信息
        ViewResult viewResult = supportAddressFeign.getSupportCity(cityEnName);
        modelAndView.addObject("city", viewResult.getData());

        //当前城市支持区域信息
        viewResult = supportAddressFeign.getSupportRegions(cityEnName);
        modelAndView.addObject("regions", viewResult.getData());

        PageResult result = houseSearchService.mapAggregation(cityEnName);
        //当前所选区域房子总数
        modelAndView.addObject("total", result.getTotal());
        //每个区域的房数
        modelAndView.addObject("aggData", result.getResult());

        modelAndView.setViewName("rent-map");
        return modelAndView;
    }

    /**
     * 根据范围查询房源列表数据
     * @param searchVo
     * @return
     */
    @GetMapping("rent/house/map/houses")
    @ResponseBody
    public ViewResult mapHouses(MapSearchVo searchVo){
        if (StringUtils.isBlank(searchVo.getCityEnName())){
            return ViewResultUtil.getUnSuccess(ViewResult.Status.BADREQUEST.getCode(), "必选城市");
        }
        PageResult result = houseSearchService.mapHouses(searchVo);
        return ViewResult.SUCCESS(result);
    }

}
