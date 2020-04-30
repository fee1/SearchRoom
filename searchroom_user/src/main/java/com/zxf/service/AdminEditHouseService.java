package com.zxf.service;

import com.zxf.DTO.HouseSearchDTO;
import com.zxf.VO.HouseSearchVo;
import com.zxf.serviceResult.ServiceResult;

import javax.servlet.http.HttpServletRequest;

public interface AdminEditHouseService {

    /**
     * 房源信息
     * @param request
     * @return
     */
    public HouseSearchDTO houses(HouseSearchVo houseSearchVo, HttpServletRequest request);


    /**
     * 删除照片
     * @return
     */
    ServiceResult deletePhoto(String id);

    /**
     * 删除上传的本地文件
     * @param fileName
     * @return
     */
    public ServiceResult deletePhotoInLocal(String fileName);

    /**
     * 删除上传的云文件
     * @param fileName
     * @return
     */
    public ServiceResult deletePhotoToClouds(String fileName);

}
