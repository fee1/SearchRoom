package com.zxf.feignclient;

import com.zxf.feignclient.impl.SupportSubwayFeignImpl;
import com.zxf.viewResult.ViewResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(value = "house-service", fallback = SupportSubwayFeignImpl.class)
public interface SupportSubwayFeign {

    /**
     * 获取支持的地铁线路
     * @param cityName
     * @return
     */
    @GetMapping("address/support/subway/line/{cityName}")
    @ResponseBody
    public ViewResult getSupportSubways(@PathVariable("cityName") String cityName);

    /**
     * 获取对应地铁线路的站点
     * @param subwayLineId
     * @return
     */
    @GetMapping("address/support/subwayStation/{subwayLineId}")
    @ResponseBody
    public ViewResult getSupportSubwayStations(@PathVariable("subwayLineId") String subwayLineId);

    /**
     * 地铁具体数据
     * @param subwayLineId
     * @return
     */
    @GetMapping("address/support/subway/lineDetail/{subwayLineId}")
    @ResponseBody
    public ViewResult getSupportSubway(@PathVariable("subwayLineId") Integer subwayLineId);

    /**
     * 获取具体地铁站点数据
     * @param subwayLineId
     * @return
     */
    @GetMapping("address/support/subwayStationDetail/{subwayStationId}")
    @ResponseBody
    public ViewResult getSupportSubwayStation(@PathVariable("subwayStationId") String subwayStationId);
}
