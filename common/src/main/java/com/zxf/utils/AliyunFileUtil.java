package com.zxf.utils;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectResult;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.BufferedInputStream;
import java.io.InputStream;

@ConfigurationProperties(prefix = "aliyunoos.config")
public class AliyunFileUtil {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    private String objectName;

    /**
     * 上传文件
     * @param filename 文件名
     * @param inputStream 文件流
     * @throws Exception
     */
    public void Upload(String filename, InputStream inputStream)throws Exception{
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        PutObjectResult result = ossClient.putObject(bucketName, objectName+filename,  bufferedInputStream);
        // 关闭OSSClient。
        ossClient.shutdown();
    }

    /**
     * 删除文件
     * @param filename
     * @throws Exception
     */
    public void delete(String filename)throws Exception{
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        ossClient.deleteObject(bucketName, objectName+filename);
        ossClient.shutdown();
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

}
