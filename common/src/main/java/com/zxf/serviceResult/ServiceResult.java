package com.zxf.serviceResult;

public class ServiceResult {

    private int status;
    private String message;
    private Object data;

    public ServiceResult() {
    }

    public ServiceResult(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public ServiceResult(int status, String message, Object data) {
        this.message = message;
        this.data = data;
    }

    public static ServiceResult seccess(String message){
        return new ServiceResult(Status.SUCCESS.getCode(), message);
    }

    public static ServiceResult seccess(String message, Object data){
        return new ServiceResult(Status.SUCCESS.getCode(), message, data);
    }

    public static ServiceResult failure(int status, String message){
        return new ServiceResult(status, message);
    }

    public enum Status{
        SUCCESS(0 , "成功"),
        ERROR(-1, "严重错误"),
        EXCEPTION(-2, "异常"),
        PARAMERROR(-3, "参数不正确");
        private int code;
        private String message;

        Status(int code, String message){
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
}
