package com.zxf.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zxf.Constant;
import com.zxf.DO.SupportAddress;
import com.zxf.DTO.BaiduMapLocation;
import com.zxf.DTO.HouseDTO;
import com.zxf.dao.SupportAddressMapper;
import com.zxf.service.HouseService;
import com.zxf.service.LBSService;
import com.zxf.serviceResult.ServiceResult;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zxf
 * 云麻点服务
 */
@Aspect
@Component
public class LBSServiceImpl implements LBSService {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    SupportAddressMapper supportAddressMapper;

    @Value("${BAIDU_MAP_KEY}")
    String BAIDU_MAP_KEY;

    @Autowired
    HouseService houseService;

    /**
     * 上传LBS云麻点
     * @return
     */
    @AfterReturning(value = "execution(public * com.zxf.service.HouseService.editHouseStatus(String, String)) && args(houseId, type))", returning = "serviceResult")
    public void lbsUpload( String houseId, String type, ServiceResult serviceResult) {
        if (serviceResult.getStatus() == ServiceResult.Status.SUCCESS.getCode() && Integer.valueOf(type) == 1) {
            HouseDTO houseDTO = houseService.findHouse(houseId, null);
            SupportAddress city = supportAddressMapper.selectSupportCity(houseDTO.getCityEnName());
            String address = houseDTO.getHouseDetail().getAddress();
            BaiduMapLocation location = this.getBaiduMapLocation(city.getCnName(), address);
            String title = houseDTO.getTitle();
            int price = houseDTO.getPrice();
            int area = houseDTO.getArea();
            HttpClient httpClient = HttpClients.createDefault();
            List<NameValuePair> nvps = new ArrayList<>();
            nvps.add(new BasicNameValuePair("latitude", String.valueOf(location.getLat())));
            nvps.add(new BasicNameValuePair("longitude", String.valueOf(location.getLon())));
            nvps.add(new BasicNameValuePair("coord_type", "3")); // 百度坐标系
            nvps.add(new BasicNameValuePair("geotable_id", "210848")); //虎鲸平台数据集id
            nvps.add(new BasicNameValuePair("ak", BAIDU_MAP_KEY));
            nvps.add(new BasicNameValuePair("houseId",houseId));
            nvps.add(new BasicNameValuePair("price", String.valueOf(price)));
            nvps.add(new BasicNameValuePair("area", String.valueOf(area)));
            nvps.add(new BasicNameValuePair("title", title));
            nvps.add(new BasicNameValuePair("address", address));

            HttpPost post;
//            if (isLbsDataExists(Long.valueOf(houseId))) {
//                post = new HttpPost(Constant.LBS_UPDATE_API);
//            } else {
//
//            }
            post = new HttpPost(Constant.LBS_CREATE_API);

            try {
                post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
                HttpResponse response = httpClient.execute(post);
                String result = EntityUtils.toString(response.getEntity(), "UTF-8");
                if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                    System.out.println("Http请求失败");
//                logger.dubug("Http请求失败");
                } else {
                    JsonNode jsonNode = objectMapper.readTree(result);
                    int status = jsonNode.get("status").asInt();
                    if (status != 0) {
                        String message = jsonNode.get("message").asText();
                        System.out.println("添加LBS失败:"+message);
//                    logger.dubug("添加LBS失败");
                    } else {
                        System.out.println("成功");
//                    logger.dubug("成功");
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
                //logger.dubug(ServiceResult.Status.EXCEPTION.getMessage());
                System.out.println("异常");
            }
        }

    }

    /**
     * 云麻点LBS是是否存在   poi数据查询服务存在问题
     * @param houseId
     * @return
     */
    public boolean isLbsDataExists(Long houseId) {
        HttpClient httpClient = HttpClients.createDefault();
        StringBuilder sb = new StringBuilder(Constant.LBS_QUERY_API);
        sb.append("geotable_id=").append("210848").append("&")
                .append("ak=").append(BAIDU_MAP_KEY).append("&")
                .append("houseId=").append(houseId);
        HttpGet get = new HttpGet(sb.toString());
        try {
            HttpResponse response = httpClient.execute(get);
            String result = EntityUtils.toString(response.getEntity(), "UTF-8");
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                return false;
            }

            JsonNode jsonNode = objectMapper.readTree(result);
            int status = jsonNode.get("status").asInt();
            if (status != 0) {
                return false;
            } else {
                long size = jsonNode.get("size").asLong();
                if (size > 0) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除LBS云麻点 poi服务存在问题
     * @param houseId
     * @return
     */
    @AfterReturning(value = "execution(public * com.zxf.service.HouseService.editHouseStatus(String, String)) && args(houseId, type))", returning = "serviceResult")
    public void removeLbs(String houseId, String type, ServiceResult serviceResult) {
        if (serviceResult.getStatus() == ServiceResult.Status.SUCCESS.getCode() && Integer.valueOf(type) != 1) {
            HttpClient httpClient = HttpClients.createDefault();
            List<NameValuePair> nvps = new ArrayList<>();
            nvps.add(new BasicNameValuePair("geotable_id", "210848"));
            nvps.add(new BasicNameValuePair("ak", BAIDU_MAP_KEY));
            nvps.add(new BasicNameValuePair("houseId", String.valueOf(houseId)));

            HttpPost delete = new HttpPost(Constant.LBS_DELETE_API);
            try {
                delete.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
                HttpResponse response = httpClient.execute(delete);
                String result = EntityUtils.toString(response.getEntity(), "UTF-8");
                if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                    System.out.println("Http请求失败");
//                    logger.dubug("Http请求失败");
                }

                JsonNode jsonNode = objectMapper.readTree(result);
                int status = jsonNode.get("status").asInt();
                if (status != 0) {
                    String message = jsonNode.get("message").asText();
                    System.out.println("更新LBS失败:"+message);
//                    logger.dubug("添加LBS失败");
                }
                System.out.println("成功");
//                logger.dubug("成功");
            } catch (IOException e) {
//                logger.dubug(ServiceResult.Status.EXCEPTION.getMessage());
                System.out.println("异常");
            }
        }
    }

    /**
     * 获取地图地理编码经纬度
     * @param city
     * @param address
     * @return
     */
    @Override
    public BaiduMapLocation getBaiduMapLocation(String city, String address){
        String encodeCity;
        String encodeAddress;

        try {
            encodeAddress = URLEncoder.encode(address, "UTF-8");
            encodeCity = URLEncoder.encode(city, "UTF-8");
        }catch (Exception e){
            e.printStackTrace();
            return new BaiduMapLocation();
        }

        HttpClient httpClient = HttpClients.createDefault();
        StringBuilder sb = new StringBuilder(Constant.BAIDU_MAP_GEOCONV_API);
        sb.append("address=").append(encodeAddress).append("&")
                .append("city=").append(encodeCity).append("&")
                .append("output=json&")
                .append("ak=").append(BAIDU_MAP_KEY);
        HttpGet httpGet = new HttpGet(sb.toString());
        try {
            HttpResponse response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                return new BaiduMapLocation();
            }
            String result = EntityUtils.toString(response.getEntity(), "UTF-8");
            JsonNode jsonNode = objectMapper.readTree(result);

            int status = jsonNode.get("status").asInt();
            if (status != 0) {
                return new BaiduMapLocation();
            } {
                BaiduMapLocation location = new BaiduMapLocation();
                JsonNode jsonLocation = jsonNode.get("result").get("location");
                location.setLon(jsonLocation.get("lng").asDouble());
                location.setLat(jsonLocation.get("lat").asDouble());
                return location;
            }
        }catch (Exception e){
            e.printStackTrace();
            return new BaiduMapLocation();
        }

    }

}
