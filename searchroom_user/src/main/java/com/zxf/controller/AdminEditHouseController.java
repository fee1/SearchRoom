package com.zxf.controller;

import com.alibaba.fastjson.JSON;
import com.zxf.DTO.HouseSearchDTO;
import com.zxf.VO.HouseSearchVo;
import com.zxf.VO.HouseVo;
import com.zxf.feignclient.HouseFeign;
import com.zxf.feignclient.SupportAddressFeign;
import com.zxf.feignclient.SupportSubwayFeign;
import com.zxf.service.AdminEditHouseService;
import com.zxf.service.UserService;
import com.zxf.serviceResult.ServiceResult;
import com.zxf.utils.ViewResultUtil;
import com.zxf.viewResult.ViewResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 房屋管理
 * @author zxf
 */
@Controller
@RequestMapping("admin/house")
public class AdminEditHouseController {

    @Autowired
    AdminEditHouseService adminEditHouseService;

    @Autowired
    UserService userService;


    @Autowired
    HouseFeign houseFeign;

    @Autowired
    SupportAddressFeign supportAddressFeign;

    @Autowired
    SupportSubwayFeign supportSubwayFeign;

    /**
     * 图片删除
     * @param id
     * @return
     */
    @DeleteMapping("delete/photo/{id}")
    @ResponseBody
    public ViewResult deletePhoto(@PathVariable("id") String id){
        ViewResult result = null;
        if (StringUtils.isBlank(id)){
            result = new ViewResult();
            result.setCode(ViewResult.Status.BADREQUEST.getCode());
            result.setMessage("文件id为空");
            return result;
        }
        //删除在云或者本地的图片
        ServiceResult serviceResult = adminEditHouseService.deletePhoto(id);
        return ViewResultUtil.getViewResult(serviceResult);
    }

    /***
     * 展示自己发布的房源列表
     * @return
     */
    @PostMapping("list")
    @ResponseBody
    public HouseSearchDTO houses(HouseSearchVo houseSearchVo, HttpServletRequest request){
        HouseSearchDTO result = adminEditHouseService.houses(houseSearchVo, request);
        return result;
    }

    /**
     * 添加房子的标签信息
     * @param id 房屋id
     * @return
     */
    @PostMapping("tag")
    @ResponseBody
    public ViewResult addHouseTag(@RequestParam("house_id") String id, @RequestParam("tag") String tag){
        ViewResult viewResult = new ViewResult();
        if (StringUtils.isBlank(id) || StringUtils.isBlank(tag)){
            viewResult.setMessage("房屋id或标签为空");
            viewResult.setCode(ViewResult.Status.BADREQUEST.getCode());
            return viewResult;
        }
        viewResult = houseFeign.addHouseTag(id, tag);
        return viewResult;
    }

    /**
     * 添加房子的标签信息
     * @param id 房屋id
     * @return
     */
    @DeleteMapping("tag")
    @ResponseBody
    public ViewResult deleteHouseTag(@RequestParam("house_id") String id, @RequestParam("tag") String tag){
        ViewResult viewResult = new ViewResult();
        if (StringUtils.isBlank(id) || StringUtils.isBlank(tag)){
            viewResult.setMessage("房屋id或标签为空");
            viewResult.setCode(ViewResult.Status.BADREQUEST.getCode());
            return viewResult;
        }
        viewResult = houseFeign.deleteHouseTag(id, tag);
        return viewResult;
    }

    /**
     * 设置封面
     * @param coverId 封面id
     * @param targetId 目标房屋id
     * @return
     */
    @PostMapping("cover")
    @ResponseBody
    public ViewResult setHouseCover(@RequestParam("cover_id") String coverId,@RequestParam("target_id") String targetId){
        ViewResult result = new ViewResult();
        if (StringUtils.isBlank(coverId)){
            result.setMessage("封面id缺失");
            result.setCode(ViewResult.Status.BADREQUEST.getCode());
            return result;
        }
        if (StringUtils.isBlank(targetId)){
            result.setMessage("房屋id缺失");
            result.setCode(ViewResult.Status.BADREQUEST.getCode());
            return result;
        }
        result = houseFeign.setHouseCover(coverId, targetId);
        return result;
    }

    /**
     * 房屋编辑修改
     * @return
     */
    @PostMapping("edit")
    @ResponseBody
    public ViewResult editHouse(HouseVo houseVo){
        ViewResult viewResult = houseFeign.editHouse(houseVo);
        return viewResult;
    }

    /**
     * 房屋发布or删除or出租
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
        viewResult = houseFeign.editHouseStatus(houseId, type);
        return viewResult;
    }



}
