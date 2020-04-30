package com.zxf.service.impl;

import com.zxf.DTO.HouseSearchDTO;
import com.zxf.VO.HouseSearchVo;
import com.zxf.feignclient.HouseFeign;
import com.zxf.service.AdminEditHouseService;
import com.zxf.utils.UserUtil;
import com.zxf.serviceResult.ServiceResult;
import com.zxf.utils.AliyunFileUtil;
import com.zxf.viewResult.ViewResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * 房屋管理
 * @author zxf
 */
@Service
public class AdminEditHouseServiceImpl implements AdminEditHouseService {

    @Autowired
    UserUtil userUtil;

    @Autowired
    AliyunFileUtil aliyunFileUtil;

    @Autowired
    HouseFeign houseFeign;

    /**
     * 房源信息列表
     * @param request
     * @return
     */
    @Override
    public HouseSearchDTO houses(HouseSearchVo houseSearchVo, HttpServletRequest request) {
        HouseSearchDTO result = new HouseSearchDTO();
        //获取用户id
        String id = userUtil.getUserId(request);
        houseSearchVo.setId(id);
        result = houseFeign.houses(houseSearchVo);
        return result;
    }

    /**
     * 删除照片
     * @return
     */
    @Override
    public ServiceResult deletePhoto(String id) {
        ViewResult result = houseFeign.deletePhoto(id);
        ServiceResult serviceResult = new ServiceResult();
        if (result.getCode() == ViewResult.Status.SUCCESS.getCode()){
            String photoName = String.valueOf(result.getData());
            if (photoName.contains("clouds")){
                serviceResult = deletePhotoToClouds(photoName);
            }else{
                serviceResult = deletePhotoInLocal(photoName);
            }
            return serviceResult;
        }else {
            return ServiceResult.failure(ServiceResult.Status.EXCEPTION.getCode(), "删除失败");
        }
    }

    /**
     * 删除本地图片
     * @param fileName
     * @return
     */
    @Override
    public ServiceResult deletePhotoInLocal(String fileName) {
        ServiceResult result = new ServiceResult();
        try {
            String classPath = AdminServiceImpl.class.getResource("/").getPath();
            String savePath = classPath +fileName;
            File localFile = new File(savePath);
            if (localFile.exists()){
                localFile.delete();
            }
        }catch (Exception e){
            e.printStackTrace();
            return ServiceResult.failure(ServiceResult.Status.EXCEPTION.getCode(), "删除失败");
        }
        return ServiceResult.seccess("删除成功");
    }

    /**
     * 删除云图片
     * @param fileName
     * @return
     */
    @Override
    public ServiceResult deletePhotoToClouds(String fileName) {
        try {
            aliyunFileUtil.delete(fileName);
        }catch (Exception e){
            e.printStackTrace();
            return ServiceResult.failure(ServiceResult.Status.EXCEPTION.getCode(), "删除失败");
        }
        return ServiceResult.seccess("删除成功");
    }


}
