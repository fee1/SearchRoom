package com.zxf.utils;

import com.zxf.serviceResult.ServiceResult;
import com.zxf.viewResult.ViewResult;

/**
 * 逻辑层结果转view结果
 * @author zxf
 */
public class ViewResultUtil {

    /**
     * 统一处理service转view
     * @return
     */
    public static ViewResult getViewResult(ServiceResult serviceResult){
        ViewResult result = new ViewResult();
        if (serviceResult.getStatus() == ServiceResult.Status.SUCCESS.getCode()){
            if (serviceResult.getData() == null) {
                return ViewResult.SUCCESS();
            }else{
                return ViewResult.SUCCESS(serviceResult.getData());
            }
        }else if ((serviceResult.getStatus() == ServiceResult.Status.ERROR.getCode()) || (serviceResult.getStatus() == ServiceResult.Status.EXCEPTION.getCode())){
            result.setCode(ViewResult.Status.SERVERERROR.getCode());
            result.setMessage(ViewResult.Status.SERVERERROR.getMessage());
            return result;
        }else if (serviceResult.getStatus() == ServiceResult.Status.PARAMERROR.getCode()){
            result.setCode(ViewResult.Status.BADREQUEST.getCode());
            result.setMessage(serviceResult.getMessage());
            result.setData(true);
            return result;
        }
        return null;
    }

    public static ViewResult getUnSuccess(int code, String message){
        ViewResult result = new ViewResult();
        result.setMessage(message);
        result.setCode(code);
        return result;
    }

}
