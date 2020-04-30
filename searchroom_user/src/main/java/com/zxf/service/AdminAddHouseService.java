package com.zxf.service;

import com.zxf.VO.HouseVo;
import com.zxf.serviceResult.ServiceResult;
import com.zxf.viewResult.ViewResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface AdminAddHouseService {

    /**
     * 上传图片到本地空间
     * @param file
     * @return
     */
    public ServiceResult uploadPhotoInLocal(MultipartFile file);

    /**
     * 上传图片到云存储空间
     * @param file
     * @return
     */
    public ServiceResult uploadPhotoToClouds(MultipartFile file);


    /**
     * 添加租赁信息
     * @return
     */
    public ViewResult addHouse(HouseVo houseVo, HttpServletRequest request);
}
