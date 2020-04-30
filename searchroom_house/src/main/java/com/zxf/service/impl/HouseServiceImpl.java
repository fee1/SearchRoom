package com.zxf.service.impl;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zxf.Constant;
import com.zxf.DO.*;
import com.zxf.DTO.*;
import com.zxf.VO.HouseSearchVo;
import com.zxf.VO.HouseVo;
import com.zxf.VO.PhotoVo;
import com.zxf.VO.RentHouseSearchVo;
import com.zxf.dao.*;
import com.zxf.feignclient.UserFeign;
import com.zxf.service.HouseService;
import com.zxf.service.LBSService;
import com.zxf.serviceResult.ServiceResult;
import com.zxf.utils.CookieUtils;
import com.zxf.utils.JwtUtil;
import com.zxf.viewResult.ViewResult;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class HouseServiceImpl implements HouseService {

    @Autowired
    HouseMapper houseMapper;

    @Autowired
    HouseDetailMapper houseDetailMapper;

    @Autowired
    HousePictureMapper housePictureMapper;

    @Autowired
    HouseTagMapper houseTagMapper;

    @Autowired
    SubwayMapper subwayMapper;

    @Autowired
    SubwayStationMapper subwayStationMapper;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    SupportAddressMapper supportAddressMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    LBSService lbsService;

    @Autowired
    UserFeign userFeign;

    @Autowired
    JwtUtil jwtUtil;

    /**
     * 添加房屋租赁信息
     * @param houseVo
     * @param id
     * @return
     */
    @Override
    public ServiceResult addHouse(HouseVo houseVo, String id) {
        ServiceResult result = checkHouseParam(houseVo);
        if (result.getStatus() == ServiceResult.Status.PARAMERROR.getCode()){
            return result;
        }
        result = checkHouseDetail(houseVo);
        if (result.getStatus() == ServiceResult.Status.PARAMERROR.getCode()){
            return result;
        }
        Date date = new Date();
        //获取封面图片名
        String coverName = houseVo.getCover().substring(houseVo.getCover().indexOf("clouds"));
        //房屋信息
        House house = new House();
        BeanUtils.copyProperties(houseVo, house);
        house.setAdminId(Integer.valueOf(id));
        house.setCover(coverName);
        houseMapper.insertHouse(house);
        houseVo.setId(house.getId());

        //房屋详细信息
        HouseDetail houseDetail = new HouseDetail();
        houseDetail.setHouseId(house.getId());
        BeanUtils.copyProperties(houseVo, houseDetail);
        houseDetail.setAddress(houseVo.getDetailAddress());
        Subway subway = subwayMapper.selectByPrimaryKey(houseVo.getSubwayLineId());
        houseDetail.setSubwayLineName(subway.getName());
        SubwayStation subwayStation = subwayStationMapper.selectByPrimaryKey(houseVo.getSubwayStationId());
        houseDetail.setSubwayStationName(subwayStation.getName());
        //添加房屋百度地图location数据
        SupportAddress city = supportAddressMapper.selectSupportCity(houseVo.getCityEnName());
        String address = houseVo.getDetailAddress();
        BaiduMapLocation location = lbsService.getBaiduMapLocation(city.getCnName(), address);
        houseDetail.setBaiduMapLat(location.getLat());
        houseDetail.setBaiduMapLng(location.getLon());
        houseDetailMapper.insertHouseDetail(houseDetail);

        //房屋图片信息
        List<HousePicture> housePictures = new ArrayList<>();
        for (PhotoVo photoVo : houseVo.getPhotos()){
            String photoName = photoVo.getPath().substring(photoVo.getPath().indexOf("clouds"));
            HousePicture housePicture = new HousePicture();
            housePicture.setHouseId(house.getId());
            BeanUtils.copyProperties(photoVo, housePicture);
            //图片名称
            housePicture.setPath(photoName);
            //图片路径前缀
            housePicture.setCdnPrefix(photoVo.getPath().substring(0 , photoVo.getPath().indexOf("clouds")));
            housePictures.add(housePicture);
        }
        housePictureMapper.insertBatchHousePictures(housePictures);

        //房屋标签
        List<HouseTag> houseTags = new ArrayList<>();
        for (String tag : houseVo.getTags()){
            HouseTag houseTag = new HouseTag();
            houseTag.setHouseId(house.getId());
            houseTag.setName(tag);
            houseTags.add(houseTag);
        }
        houseTagMapper.insertBatchHouseTags(houseTags);
        result.setMessage("添加成功");
        result.setStatus(ServiceResult.Status.SUCCESS.getCode());


//        //通知ES创建索引数据
//        HouseDTO houseDTO = new HouseDTO();
//        BeanUtils.copyProperties(house, houseDTO);
//
//        HouseDetailDTO houseDetailDTO = new HouseDetailDTO();
//        BeanUtils.copyProperties(houseDetail ,houseDetailDTO);
//        houseDTO.setHouseDetail(houseDetailDTO);
//
//        houseDTO.setTags(houseVo.getTags());
//
//        Map<String, String> map = new HashMap<>();
//        map.put(HouseConstant.create, JSON.toJSONString(houseDTO));
//        rabbitTemplate.convertAndSend("searchroom_house", map);

        return result;
    }


    /**
     * admin房源信息展示
     * @param
     * @return
     */
    @Override
    public Page houses(HouseSearchVo houseSearchVo) {
        Page page = PageHelper.startPage(houseSearchVo.getStart() / houseSearchVo.getLength()+1, houseSearchVo.getLength(), true);
        houseMapper.selectHousesByAdmin(houseSearchVo);
        return page;
    }

    @Value("${token_name}")
    String TOKENNAME;

    /**
     * 根据房屋id查找房子
     * @param id
     * @return
     */
    @Override
    public HouseDTO findHouse(String id, HttpServletRequest request) {
        HouseDTO houseDTO = new HouseDTO();

        //房屋基本信息
        House house = houseMapper.selectByPrimaryKey(Integer.valueOf(id));
        BeanUtils.copyProperties(house, houseDTO);
        houseDTO.setCover(Constant.aliImage+house.getCover());
        //房屋详细信息
        HouseDetail houseDetail = houseDetailMapper.selectByHouseId(id);
        HouseDetailDTO houseDetailDTO = new HouseDetailDTO();
        BeanUtils.copyProperties(houseDetail, houseDetailDTO);
        houseDTO.setHouseDetail(houseDetailDTO);
        //房屋照片信息
        List<HousePicture> housePictures = housePictureMapper.selectByHouseId(id);
        List<HousePictureDTO> housePictureDTOS = new ArrayList<>();
        for (HousePicture housePicture : housePictures){
            HousePictureDTO housePictureDTO = new HousePictureDTO();
            BeanUtils.copyProperties(housePicture, housePictureDTO);
            housePictureDTOS.add(housePictureDTO);
        }
        houseDTO.setPictures(housePictureDTOS);

        //房屋标签信息
        List<HouseTag> houseTags = houseTagMapper.selectByHouseId(id);
        List<String> tags = new ArrayList<>();
        for (HouseTag tag : houseTags){
            tags.add(tag.getName());
        }
        houseDTO.setTags(tags);

        //房子预约状态
        if (request != null) {
            Claims claims = jwtUtil.parseJWT(CookieUtils.getCookieValue(request, TOKENNAME));
            String userId = claims.getId();
            ViewResult viewResult = userFeign.findSubscribeStatus(id, userId);
            houseDTO.setSubscribeStatus(JSON.parseObject(JSON.toJSONString(viewResult.getData()), Integer.class));
        }

        return houseDTO;
    }

    /**
     * 删除数据库照片数据
     * @param id
     * @return
     */
    @Override
    public ServiceResult deletePhoto(String id) {
        HousePicture picture = housePictureMapper.selectByPrimaryKey(Integer.valueOf(id));
        housePictureMapper.deleteByPrimaryKey(Integer.valueOf(id));
        return ServiceResult.seccess("删除成功", picture.getPath());
    }

    /**
     * 房屋信息编辑
     * @param houseVo
     * @return
     */
    @Override
    public ServiceResult editHouse(HouseVo houseVo) {
        ServiceResult result = checkHouseParam(houseVo);
        if (result.getStatus() == ServiceResult.Status.PARAMERROR.getCode()){
            return result;
        }
        result = checkHouseDetail(houseVo);
        if (result.getStatus() == ServiceResult.Status.PARAMERROR.getCode()){
            return result;
        }
        Date date = new Date();
        //房屋信息
        House house = new House();
        BeanUtils.copyProperties(houseVo, house);
        //获取封面图片名
        if (houseVo.getCover() != null) {
            String coverName = houseVo.getCover().substring(houseVo.getCover().indexOf("clouds"));
            house.setCover(coverName);
        }
        houseMapper.updateHouse(house);

        //房屋详细信息
        HouseDetail houseDetail = new HouseDetail();
        houseDetail.setHouseId(houseVo.getId());
        BeanUtils.copyProperties(houseVo, houseDetail);
        houseDetail.setAddress(houseVo.getDetailAddress());
        Subway subway = subwayMapper.selectByPrimaryKey(houseVo.getSubwayLineId());
        houseDetail.setSubwayLineName(subway.getName());
        SubwayStation subwayStation = subwayStationMapper.selectByPrimaryKey(houseVo.getSubwayStationId());
        houseDetail.setSubwayStationName(subwayStation.getName());
        houseDetailMapper.updateHouseDetail(houseDetail);

        //房屋图片信息
        List<HousePicture> housePictures = new ArrayList<>();
        if (houseVo.getPhotos() != null) {
            for (PhotoVo photoVo : houseVo.getPhotos()) {
                String photoName = photoVo.getPath().substring(photoVo.getPath().indexOf("clouds"));
                HousePicture housePicture = new HousePicture();
                housePicture.setHouseId(house.getId());
                BeanUtils.copyProperties(photoVo, housePicture);
                //图片名称
                housePicture.setPath(photoName);
                //图片路径前缀
                housePicture.setCdnPrefix(photoVo.getPath().substring(0, photoVo.getPath().indexOf("clouds")));
                housePictures.add(housePicture);
            }
            housePictureMapper.insertBatchHousePictures(housePictures);
        }

        result.setMessage("修改成功");
        result.setStatus(ServiceResult.Status.SUCCESS.getCode());
        return result;
    }

    /**
     * 房屋标签添加
     * @param id
     * @return
     */
    @Override
    public ServiceResult addHouseTag(String id, String tag) {
        HouseTag houseTag = new HouseTag();
        houseTag.setName(tag);
        houseTag.setHouseId(Integer.valueOf(id));
        int i = houseTagMapper.insert(houseTag);
        if (i >0) {
            return ServiceResult.seccess("成功");
        }else{
            return ServiceResult.failure(ServiceResult.Status.EXCEPTION.getCode(), "添加失败");
        }
    }

    /**
     * 房屋标签删除
     * @param id
     * @param tag
     * @return
     */
    @Override
    public ServiceResult deleteHouseTag(String id, String tag) {
        int i = houseTagMapper.deleteTagByTagNameAndHouseId(id, tag);
        if (i >0) {
            return ServiceResult.seccess("成功");
        }else{
            return ServiceResult.failure(ServiceResult.Status.EXCEPTION.getCode(), "删除失败");
        }
    }

    /**
     * 房屋封面设置
     * @param coverId
     * @param targetId
     * @return
     */
    @Override
    public ServiceResult setHouseCover(String coverId, String targetId) {
        HousePicture picture = housePictureMapper.selectByPrimaryKey(Integer.valueOf(coverId));
        if (picture == null){
            return ServiceResult.failure(ServiceResult.Status.EXCEPTION.getCode(), "没有找到图片数据");
        }
        House house = new House();
        house.setId(Integer.valueOf(targetId));
        house.setCover(picture.getPath());
        int i = houseMapper.updateHouse(house);
        if (i >0) {
            return ServiceResult.seccess("成功");
        }else{
            return ServiceResult.failure(ServiceResult.Status.EXCEPTION.getCode(), "设置失败");
        }
    }

    /**
     * 改变房屋状态
     * @param houseId
     * @param type
     * @return
     */
    @Override
    public ServiceResult editHouseStatus(String houseId, String type) {
        House house = houseMapper.selectByPrimaryKey(Integer.valueOf(houseId));
        house.setStatus(Integer.valueOf(type));
        int i = houseMapper.updateHouse(house);
        if (i >0) {
            return ServiceResult.seccess("成功");
        }else{
            return ServiceResult.failure(ServiceResult.Status.EXCEPTION.getCode(), "失败");
        }
    }

    /**
     * user房源信息展示
     * @param searchVo
     * @return
     */
    @Override
    public Page houses(RentHouseSearchVo searchVo) {
        Page page = PageHelper.startPage(searchVo.getStart() / searchVo.getSize()+1, searchVo.getSize(), true);
        searchVo.setMinPrice(RentValueBlockDTO.matchPrice(searchVo.getPriceBlock()).getMin());
        searchVo.setMaxPrice(RentValueBlockDTO.matchPrice(searchVo.getPriceBlock()).getMax());
        searchVo.setMinArea(RentValueBlockDTO.matchArea(searchVo.getAreaBlock()).getMin());
        searchVo.setMaxArea(RentValueBlockDTO.matchArea(searchVo.getAreaBlock()).getMax());
        houseMapper.selectHousesByUser(searchVo);
        List<HouseDTO> houseDTOS= page.getResult();
        //查询标签信息
        for (HouseDTO houseDTO : houseDTOS) {
            List<HouseTag> houseTags = houseTagMapper.selectByHouseId(String.valueOf(houseDTO.getId()));
            List<String> tags = new ArrayList<>();
            for (HouseTag houseTag : houseTags){
                tags.add(houseTag.getName());
            }
            houseDTO.setTags(tags);
            houseDTO.setCover(Constant.aliImage+houseDTO.getCover());
        }
        return page;
    }

    /**
     * 当前小区几套在出租
     * @param cityEnName
     * @param regionEnName
     * @param district
     * @return
     */
    @Override
    public int getRentHouseSum(String cityEnName, String regionEnName, String district) {
        int sum = houseMapper.getRentHouseSum(cityEnName, regionEnName, district);
        return sum;
    }

    /**
     * 根据ID查询房源数据
     * @param houseIds
     * @return
     */
    @Override
    public ServiceResult findHouseDTOS(String houseIds) {
        List<HouseDTO> houseDTOS = houseMapper.findHouseDTOS(houseIds);
        for (HouseDTO houseDTO: houseDTOS){
            houseDTO.setCover(Constant.aliImage+houseDTO.getCover());
        }
        return ServiceResult.seccess("成功", houseDTOS);
    }


    /**
     * 添加房屋信息检查
     * @return
     */
    public ServiceResult checkHouseParam(HouseVo houseVo){
        ServiceResult result = new ServiceResult();
        if (StringUtils.isBlank(houseVo.getTitle())){
            return ServiceResult.failure(ServiceResult.Status.PARAMERROR.getCode(), "标题不能为空");
        }else if (houseVo.getPrice() == null || houseVo.getPrice() == 0){
            return ServiceResult.failure(ServiceResult.Status.PARAMERROR.getCode(), "价格必填");
        }else if (houseVo.getArea() == null || houseVo.getArea() ==0){
            return ServiceResult.failure(ServiceResult.Status.PARAMERROR.getCode(),"面积必填");
        }else if (houseVo.getRoom() == null){
            return ServiceResult.failure(ServiceResult.Status.PARAMERROR.getCode(), "卧室数量必填");
        }else if (houseVo.getFloor() == null || houseVo.getFloor() == 0){
            return ServiceResult.failure(ServiceResult.Status.PARAMERROR.getCode(), "楼层数量必填");
        }else if (houseVo.getTotalFloor() == null || houseVo.getTotalFloor() == 0){
            return ServiceResult.failure(ServiceResult.Status.PARAMERROR.getCode(), "总楼层数量必填");
        }else if (houseVo.getBuildYear() == null || houseVo.getBuildYear() == 0){
            return ServiceResult.failure(ServiceResult.Status.PARAMERROR.getCode(), "建立年限必填");
        }else if (StringUtils.isBlank(houseVo.getCityEnName())){
            return ServiceResult.failure(ServiceResult.Status.PARAMERROR.getCode(), "城市必填");
        }else if (StringUtils.isBlank(houseVo.getRegionEnName())){
            return ServiceResult.failure(ServiceResult.Status.PARAMERROR.getCode(), "城区必填");
        }else if (StringUtils.isBlank(houseVo.getCover()) && houseVo.getId() == null){
            return ServiceResult.failure(ServiceResult.Status.PARAMERROR.getCode(), "封面必选");
        }else if (houseVo.getDirection() == null){
            return ServiceResult.failure(ServiceResult.Status.PARAMERROR.getCode(), "房屋朝向必选");
        }else if (houseVo.getDistanceToSubway() == 0){
            return ServiceResult.failure(ServiceResult.Status.PARAMERROR.getCode(), "距地铁距离不能为0");
        }else if (StringUtils.isBlank(houseVo.getDistrict())){
            return ServiceResult.failure(ServiceResult.Status.PARAMERROR.getCode(), "所在小区必填");
        }else if (StringUtils.isBlank(houseVo.getStreet())){
            return ServiceResult.failure(ServiceResult.Status.PARAMERROR.getCode(), "街道必填");
        }
        return result;
    }

    /**
     * 详情信息检查
     * @return
     */
    public ServiceResult checkHouseDetail(HouseVo houseVo){
        ServiceResult result = new ServiceResult();
        if (houseVo.getRentWay() == null){
            return ServiceResult.failure(ServiceResult.Status.PARAMERROR.getCode(), "租赁方式不能为空");
        }else if (StringUtils.isBlank(houseVo.getDetailAddress())){
            return ServiceResult.failure(ServiceResult.Status.PARAMERROR.getCode(), "详细地址必填");
        }
        return result;
    }


}
