package com.zxf.service.impl;

import com.zxf.Constant;
import com.zxf.VO.HouseVo;
import com.zxf.VO.PhotoVo;
import com.zxf.dao.UserMapper;
import com.zxf.feignclient.HouseFeign;
import com.zxf.service.AdminAddHouseService;
import com.zxf.utils.UserUtil;
import com.zxf.serviceResult.ServiceResult;
import com.zxf.utils.AliyunFileUtil;
import com.zxf.utils.IdWorker;
import com.zxf.viewResult.ViewResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Service
public class AdminAddServiceImpl implements AdminAddHouseService {

    @Autowired
    UserUtil userUtil;

    @Autowired
    AliyunFileUtil aliyunFileUtil;

    @Value("${aliyunoos.config.url}")
    private String url;

    @Autowired
    IdWorker idWorker;

    @Autowired
    UserMapper userMapper;

    @Autowired
    HouseFeign houseFeign;

    /**
     * 上传图片到本地
     * @param file
     * @return
     */
    @Override
    public ServiceResult uploadPhotoInLocal(MultipartFile file) {
        PhotoVo photoVo = this.photoParam(file);
        String fileName = file.getOriginalFilename();
        //雪花算法保证用户上传时图片保存的名字不会重复
        String LocalFileName = String.valueOf(idWorker.nextId());
        String classPath = AdminServiceImpl.class.getResource("/").getPath();
        //获取文件扩展名
        String extName = fileName.substring(fileName.indexOf("."));
        //不是如下拓展名的文件
        if (!Constant.imageExt.contains(extName)){
            return ServiceResult.failure(ServiceResult.Status.PARAMERROR.getCode(), "上传失败，不支持此格式的文件");
        }
        String savePath = classPath +"/local"+LocalFileName;
        File localFile = new File(savePath);
        try {
            file.transferTo(localFile);
        }catch (IOException e){
            e.printStackTrace();
            return ServiceResult.failure(ServiceResult.Status.EXCEPTION.getCode(), "上传失败");
        }
        //设置照片的路径
        photoVo.setPath(savePath);
        return ServiceResult.seccess("上传成功", photoVo);
    }

    /**
     * 上传图片到云存储
     * @param file
     * @return
     */
    @Override
    public ServiceResult uploadPhotoToClouds(MultipartFile file) {
        PhotoVo photoVo = this.photoParam(file);
        ServiceResult result = new ServiceResult();
        //文件名
        String fileName = file.getOriginalFilename();
        //雪花算法保证用户上传时图片保存的名字不会重复
        String uploadFileName = String.valueOf(idWorker.nextId());
        //获取文件扩展名
        String extName = fileName.substring(fileName.indexOf("."));
        //不是如下拓展名的文件
        if (!Constant.imageExt.contains(extName)){
            return ServiceResult.failure(ServiceResult.Status.PARAMERROR.getCode(), "上传失败，不支持此格式的文件");
        }
        uploadFileName = "clouds"+uploadFileName+extName;
        try {
            aliyunFileUtil.Upload(uploadFileName, file.getInputStream());
        }catch (Exception e){
            e.printStackTrace();
            return ServiceResult.failure(ServiceResult.Status.EXCEPTION.getCode(), "上传失败");
        }
        //设置照片路径
        photoVo.setPath(url+uploadFileName);
        return ServiceResult.seccess("上传成功", photoVo);
    }

    /**
     * 获取设置图片的宽高
     * @param file
     * @return
     */
    public PhotoVo photoParam(MultipartFile file){
        PhotoVo photoVo = new PhotoVo();
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            int height = image.getHeight();
            int width = image.getWidth();
            photoVo.setHeight(height);
            photoVo.setWidth(width);
        }catch (IOException e){
            e.printStackTrace();
        }
        return photoVo;
    }


    /**
     * 添加租赁信息
     * @param houseVo
     * @param request
     * @return
     */
    @Override
    public ViewResult addHouse(HouseVo houseVo, HttpServletRequest request) {
        ViewResult result = new ViewResult();
        String id = userUtil.getUserId(request);
        result = houseFeign.addHouse(houseVo, id);
        return result;
    }


}
