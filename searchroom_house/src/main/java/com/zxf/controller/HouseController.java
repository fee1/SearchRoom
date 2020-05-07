package com.zxf.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.zxf.DTO.HouseDTO;
import com.zxf.DTO.HouseSearchDTO;
import com.zxf.VO.HouseSearchVo;
import com.zxf.VO.HouseVo;
import com.zxf.service.HouseService;
import com.zxf.serviceResult.ServiceResult;
import com.zxf.utils.ViewResultUtil;
import com.zxf.viewResult.ViewResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class HouseController {

    @Autowired
    HouseService houseService;

    /**
     * 添加租赁房源信息
     * @param houseVo 房源信息
     * @param id 用户id
     * @return
     */
    @PostMapping("add/house")
    @ResponseBody
    public ViewResult addHouse(@RequestBody HouseVo houseVo, String id){
        ViewResult result = new ViewResult();
        if (StringUtils.isBlank(id)){
            return ViewResultUtil.getUnSuccess(ViewResult.Status.BADREQUEST.getCode(), "用户id缺失");
        }
        return ViewResultUtil.getViewResult(houseService.addHouse(houseVo, id));
    }

    /**
     * 房源信息展示
     * @return
     */
    @PostMapping("house/list")
    @ResponseBody
    public HouseSearchDTO houses(@RequestBody HouseSearchVo houseSearchVo){
        HouseSearchDTO result = new HouseSearchDTO();
        Page page = houseService.houses(houseSearchVo);
        result.setData(page.getResult());
        result.setRecordsTotal(page.getTotal());
        result.setRecordsFiltered(page.getTotal());
        result.setDraw(houseSearchVo.getDraw());
        return result;
    }

    /**
     * 根据房屋id查找房子
     * @param id
     * @return
     */
    @GetMapping("house/details")
    @ResponseBody
    public HouseDTO findHouse(String id){
        if (id == null || Integer.valueOf(id)< 1){
            return null;
        }
        HouseDTO houseDTO = houseService.findHouse(id, null);
        return houseDTO;
    }

    /**
     * 删除照片
     * @param id
     * @return
     */
    @DeleteMapping("delete/photo/{id}")
    @ResponseBody
    public ViewResult deletePhoto(String id){
        ViewResult result = new ViewResult();
        if (StringUtils.isBlank(id)){
            return ViewResultUtil.getUnSuccess(ViewResult.Status.BADREQUEST.getCode(), "照片id缺失");
        }
        ServiceResult serviceResult = houseService.deletePhoto(id);
        return ViewResultUtil.getViewResult(serviceResult);
    }

    /**
     * 房屋标签添加
     * @param id 房屋id
     * @return
     */
    @PostMapping("house/tag")
    @ResponseBody
    public ViewResult addHouseTag(@RequestParam("house_id") String id,@RequestParam("tag") String tag){
        ViewResult viewResult = new ViewResult();
        if (StringUtils.isBlank(id) || StringUtils.isBlank(tag)){
            return ViewResultUtil.getUnSuccess(ViewResult.Status.BADREQUEST.getCode(), "房屋id或标签为空");
        }
        ServiceResult result = houseService.addHouseTag(id, tag);
        return ViewResultUtil.getViewResult(result);
    }

    /**
     * 房屋标签删除
     * @param id
     * @param tag
     * @return
     */
    @DeleteMapping("house/tag")
    @ResponseBody
    public ViewResult deleteHouseTag(@RequestParam("house_id") String id, @RequestParam("tag") String tag){
        ViewResult viewResult = new ViewResult();
        if (StringUtils.isBlank(id) || StringUtils.isBlank(tag)){
            return ViewResultUtil.getUnSuccess(ViewResult.Status.BADREQUEST.getCode(), "房屋id或标签为空");
        }
        ServiceResult serviceResult = houseService.deleteHouseTag(id, tag);
        return ViewResultUtil.getViewResult(serviceResult);
    }

    /**
     * 房屋封面设置
     * @param coverId 封面图片id
     * @param targetId 目标房屋id
     * @return
     */
    @PostMapping("house/cover")
    @ResponseBody
    public ViewResult setHouseCover(@RequestParam("cover_id") String coverId,@RequestParam("target_id") String targetId){
        ViewResult result = new ViewResult();
        if (StringUtils.isBlank(coverId)){
            return ViewResultUtil.getUnSuccess(ViewResult.Status.BADREQUEST.getCode(), "封面id缺失");
        }
        if (StringUtils.isBlank(targetId)){
            return ViewResultUtil.getUnSuccess(ViewResult.Status.BADREQUEST.getCode(), "房屋id缺失");
        }
        ServiceResult serviceResult = houseService.setHouseCover(coverId, targetId);
        return ViewResultUtil.getViewResult(serviceResult);
    }

    /**
     * 房屋编辑修改
     * @param houseVo
     * @return
     */
    @PostMapping("house/edit")
    @ResponseBody
    public ViewResult editHouse(@RequestBody HouseVo houseVo){
        ServiceResult result = houseService.editHouse(houseVo);
        return ViewResultUtil.getViewResult(result);
    }

    /**
     * 房屋状态修改
     * @param houseId
     * @param type
     * @return
     */
    @PutMapping("operate/{houseId}/{type}")
    @ResponseBody
    public ViewResult editHouseStatus(@PathVariable("houseId") String houseId, @PathVariable("type") String type){
        ViewResult viewResult = new ViewResult();
        if (StringUtils.isBlank(houseId)){
            viewResult.setCode(ViewResult.Status.BADREQUEST.getCode());
            viewResult.setMessage("房子ID为空");
            return viewResult;
        }
        if (StringUtils.isBlank(type)){
            viewResult.setCode(ViewResult.Status.BADREQUEST.getCode());
            viewResult.setMessage("状态为空");
            return viewResult;
        }
        ServiceResult result = houseService.editHouseStatus(houseId, type);
        return ViewResultUtil.getViewResult(result);
    }

    /**
     * 根据ID组获取房屋列表
     * @param houseIds
     * @return
     */
    @GetMapping("getHouseByIds")
    @ResponseBody
    public ViewResult findHouseDTOS(String houseIds){
        if (StringUtils.isBlank(houseIds)){
            return ViewResultUtil.getUnSuccess(ViewResult.Status.BADREQUEST.getCode(), "房屋ID为空");
        }
        ServiceResult serviceResult = houseService.findHouseDTOS(houseIds);
        return ViewResultUtil.getViewResult(serviceResult);
    }

    /**
     * 增加用户带看次数
     * @param houseId
     * @return
     */
    @PostMapping("addHouseWatchTimes")
    @ResponseBody
    public ViewResult addHouseWatchTimes(String houseId){
        if(StringUtils.isBlank(houseId)){
            return ViewResultUtil.getUnSuccess(ViewResult.Status.BADREQUEST.getCode(), "房屋ID为空");
        }
        ServiceResult serviceResult = houseService.addHouseWatchTimes(houseId);
        return ViewResultUtil.getViewResult(serviceResult);
    }

}
