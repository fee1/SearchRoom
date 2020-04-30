package com.zxf;

public interface Constant {

   /**
    * 手机号码正则
    */
   String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";

   /**
    * 图片拓展名
    */
   String imageExt = ".jpg、.png、.gif、.jpeg";

   /**
    * 阿里云图片前缀
    */
   String aliImage = "https://searchroom.oss-cn-shanghai.aliyuncs.com/images/";

   /**
    * 百度api
    */
   String BAIDU_MAP_GEOCONV_API = "http://api.map.baidu.com/geocoding/v3/?";

   /**
    * 云麻点POI数据管理接口
    */
   String LBS_CREATE_API = "http://api.map.baidu.com/geodata/v3/poi/create";

   String LBS_QUERY_API = "http://api.map.baidu.com/geodata/v3/poi/list?";

   String LBS_UPDATE_API = "http://api.map.baidu.com/geodata/v3/poi/update";

   String LBS_DELETE_API = "http://api.map.baidu.com/geodata/v3/poi/delete";

}
