package com.zxf.controller;

import com.zxf.DTO.HouseDTO;
import com.zxf.feignclient.HouseFeign;
import com.zxf.feignclient.SupportAddressFeign;
import com.zxf.feignclient.SupportSubwayFeign;
import com.zxf.viewResult.ViewResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("admin")
public class AdminPageController {

    @Autowired
    HouseFeign houseFeign;

    @Autowired
    SupportAddressFeign supportAddressFeign;

    @Autowired
    SupportSubwayFeign supportSubwayFeign;

    /**
     * 管理员登录页
     * @return
     */
    @GetMapping("loginPage")
    public String adminLoginPage(){
        return "admin/login";
    }

    /**
     * 管理员中心
     * @return
     */
    @GetMapping("center")
    public String center(){
        return "admin/center";
    }

    /**
     * welcome页面
     * @return
     */
    @GetMapping("welcome")
    public String welcome(HttpServletResponse response){
        return "admin/welcome";
    }

    /**
     * 新增房源
     * @return
     */
    @GetMapping("add/house")
    public String addHouse(){
        return "admin/house-add";
    }

    /**
     * 房源展示页
     * @return
     */
    @GetMapping("house/list")
    public String houseList(){
        return "admin/house-list";
    }

    /**
     * 房屋编辑页
     * @param id
     * @param modelAndView
     * @return
     */
    @GetMapping("house/edit")
    public ModelAndView houseEdit(@RequestParam(value = "id") String id, ModelAndView modelAndView){
        if (id == null || Integer.valueOf(id) < 1){
            modelAndView.setViewName("404");
            return modelAndView;
        }
        HouseDTO houseDTO = houseFeign.findHouse(id);
        modelAndView.addObject("house", houseDTO);

        //支持的具体城市数据
        ViewResult result = supportAddressFeign.getSupportCity(houseDTO.getCityEnName());
        modelAndView.addObject("city", result.getData());

        //支持的具体城市区域数据
        result = supportAddressFeign.getSupportRegion(houseDTO.getCityEnName(), houseDTO.getRegionEnName());
        modelAndView.addObject("region", result.getData());

        //城市具体地铁数据
        result = supportSubwayFeign.getSupportSubway(houseDTO.getHouseDetail().getSubwayLineId());
        modelAndView.addObject("subway", result.getData());

        //地铁对应站点数据
        result = supportSubwayFeign.getSupportSubwayStation(String.valueOf(houseDTO.getHouseDetail().getSubwayStationId()));
        modelAndView.addObject("station", result.getData());

        modelAndView.setViewName("admin/house-edit");
        return modelAndView;
    }

    /**
     * 预约管理页
     * @return
     */
    @GetMapping("house/subscribe")
    public String subscribe(){
        return "admin/subscribe";
    }

}
