package com.zxf.controller;

import com.zxf.service.SupportAddressService;
import com.zxf.utils.ViewResultUtil;
import com.zxf.viewResult.ViewResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SupportAddressController {

    @Autowired
    SupportAddressService supportAddressService;

    /**
     * 获取支持的城市列表
     * @return
     */
    @GetMapping("address/support/cities")
    @ResponseBody
    public ViewResult getSupportCities(){
        ViewResult result = ViewResultUtil.getViewResult(supportAddressService.getSupportCities());
        return result;
    }

    /**
     * 支持的城市具体数据
     * @return
     */
    @GetMapping("address/support/city")
    @ResponseBody
    public ViewResult getSupportCity(String cityEnName){
        if (StringUtils.isBlank(cityEnName)){
            ViewResult result = new ViewResult();
            result.setCode(ViewResult.Status.BADREQUEST.getCode());
            result.setMessage("城市名字不能为空");
            return result;
        }
        ViewResult result = ViewResultUtil.getViewResult(supportAddressService.getSupportCity(cityEnName));
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
        ViewResult result = ViewResultUtil.getViewResult(supportAddressService.getSupportRegions(cityName));
        return result;
    }

    @GetMapping("address/support/regionsDetail/{cityEnName}/{regionEnName}")
    @ResponseBody
    public ViewResult getSupportRegion(@PathVariable("cityEnName") String cityEnName, @PathVariable("regionEnName") String regionEnName){
        if (StringUtils.isBlank(cityEnName)){
            ViewResult result = new ViewResult();
            result.setCode(ViewResult.Status.BADREQUEST.getCode());
            result.setMessage("城市名字不能为空");
            return result;
        }
        if (StringUtils.isBlank(regionEnName)){
            ViewResult result = new ViewResult();
            result.setCode(ViewResult.Status.BADREQUEST.getCode());
            result.setMessage("区域名字不能为空");
            return result;
        }
        ViewResult result = ViewResultUtil.getViewResult(supportAddressService.getSupportRegion(cityEnName, regionEnName));
        return result;
    }
}
