package com.zxf.feignclient;


import com.zxf.feignclient.impl.SupportAddressFeignImpl;
import com.zxf.viewResult.ViewResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zxf
 */
@FeignClient(value = "house-service", fallback = SupportAddressFeignImpl.class)//调用服务的名称
public interface SupportAddressFeign{

    /**
     * 获取支持的城市列表
     * @return
     */
    @GetMapping("address/support/cities")
    @ResponseBody
    public ViewResult getSupportCities();

    /**
     * 支持的城市具体数据
     * @param cityEnName 城市名称
     * @return
     */
    @GetMapping("address/support/city")
    @ResponseBody
    public ViewResult getSupportCity(@RequestParam String cityEnName);

    /**
     * 获取支持对应的城市支持的区域表
     */
    @GetMapping("address/support/regions/{cityName}")
    @ResponseBody
    public ViewResult getSupportRegions(@PathVariable("cityName") String cityName);

    /**
     * 获取城市对应的具体区域数据
     * @param cityEnName
     * @param regionEnName
     * @return
     */
    @GetMapping("address/support/regionsDetail/{cityEnName}/{regionEnName}")
    @ResponseBody
    ViewResult getSupportRegion(@PathVariable("cityEnName") String cityEnName, @PathVariable("regionEnName") String regionEnName);
}
