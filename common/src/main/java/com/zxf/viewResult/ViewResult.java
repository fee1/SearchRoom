package com.zxf.viewResult;

import java.io.Serializable;

/**
 * @author 朱晓峰
 */
public class ViewResult implements Serializable {

    private int code;
    private String message;
    private Object data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public ViewResult() {
    }

    public ViewResult(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ViewResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ViewResult SUCCESS(){
        return new ViewResult(Status.SUCCESS.getCode(), Status.SUCCESS.getMessage());
    }

    public static ViewResult SUCCESS(Object data){
        return new ViewResult(Status.SUCCESS.getCode(), Status.SUCCESS.getMessage(), data);
    }

    public enum Status{
        SUCCESS(200, "请求已成功"),
        REDIRECT(302, "跳转"),
        BADREQUEST(400, "请求参数有误"),
        FORBIDDEN(403, "拒绝服务"),
        NOTFOUND(404, "请求资源不存在"),
        SERVERERROR(500, "服务器不可预期错误");
        private int code;
        private String message;

        Status(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}
