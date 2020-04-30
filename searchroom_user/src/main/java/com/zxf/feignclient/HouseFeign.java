package com.zxf.feignclient;

import com.zxf.DTO.HouseDTO;
import com.zxf.DTO.HouseSearchDTO;
import com.zxf.VO.HouseSearchVo;
import com.zxf.feignclient.impl.HouseFeignImpl;
import com.zxf.VO.HouseVo;
import com.zxf.viewResult.ViewResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@FeignClient(value = "house-service", fallback = HouseFeignImpl.class)
public interface HouseFeign {

    /**
     * 添加租赁房源信息
     * @param houseVo 房源信息
     * @param id 用户id
     * @return
     */
    @PostMapping("add/house")
    @ResponseBody
    public ViewResult addHouse(@RequestBody HouseVo houseVo,@RequestParam String id);

    /**
     * 房源信息展示
     * @return
     */
    @PostMapping("house/list")
    @ResponseBody
    public HouseSearchDTO houses(@RequestBody HouseSearchVo houseSearchVo);

    /**
     * 根据id查找房子
     * @param id
     * @return
     */
    @GetMapping("house/details")
    @ResponseBody
    public HouseDTO findHouse(@RequestParam String id);

    /**
     * 删除照片
     * @param id
     * @return
     */
    @DeleteMapping("delete/photo/{id}")
    @ResponseBody
    ViewResult deletePhoto(@RequestParam String id);

    /**
     * 房屋编辑修改
     * @param houseVo
     * @return
     */
    @PostMapping("house/edit")
    @ResponseBody
    ViewResult editHouse(@RequestBody HouseVo houseVo);

    /**
     * 房屋标签添加
     * @param id 房屋id
     * @return
     */
    @PostMapping("house/tag")
    @ResponseBody
    ViewResult addHouseTag(@RequestParam("house_id") String id, @RequestParam("tag") String tag);

    /**
     * 房屋标签删除
     * @param id
     * @param tag
     * @return
     */
    @DeleteMapping("house/tag")
    @ResponseBody
    ViewResult deleteHouseTag(@RequestParam("house_id") String id, @RequestParam("tag") String tag);

    /**
     * 房屋封面设置
     * @param coverId 封面图片id
     * @param targetId 目标房屋id
     * @return
     */
    @PostMapping("house/cover")
    @ResponseBody
    ViewResult setHouseCover(@RequestParam("cover_id") String coverId,@RequestParam("target_id") String targetId);

    /**
     * 改变房子状态
     * @param houseId
     * @param type
     * @return
     */
    @PutMapping("operate/{houseId}/{type}")
    @ResponseBody
    ViewResult editHouseStatus(@PathVariable("houseId") String houseId, @PathVariable("type") String type);

    /**
     * 根据ID组获取房屋列表
     * @param substring
     * @return
     */
    @GetMapping
    @ResponseBody
    ViewResult findHouseDTOS(@RequestParam("houseIds") String houseIds);
}
