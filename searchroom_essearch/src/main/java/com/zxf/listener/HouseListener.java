package com.zxf.listener;

import com.alibaba.fastjson.JSON;
import com.zxf.DTO.HouseDTO;
import com.zxf.HouseConstant;
import com.zxf.dao.HouseSearchDao;
import com.zxf.pojo.HouseIndex;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "searchroom_house")
public class HouseListener {

    @Autowired
    HouseSearchDao houseSearchDao;

    @RabbitHandler
    public void houseListener(Map<String, String> map){
        if (StringUtils.isNotBlank(map.get(HouseConstant.create))){

            HouseDTO houseDTO = JSON.parseObject(map.get(HouseConstant.create), HouseDTO.class);
            HouseIndex houseIndex = new HouseIndex();
            BeanUtils.copyProperties(houseDTO, houseIndex);
            houseIndex.setId(Long.valueOf(houseDTO.getId()));
            houseIndex.setHouseId(Long.valueOf(houseDTO.getId()));

            BeanUtils.copyProperties(houseDTO.getHouseDetail(), houseIndex);

            StringBuffer tags = new StringBuffer();
            for (String tag : houseDTO.getTags()){
                tags.append(tags+",");
            }
            houseIndex.setTags(tags.toString().substring(0, tags.length()-1));

            houseSearchDao.save(houseIndex);
        }
        if (StringUtils.isNotBlank(map.get(HouseConstant.update))){

            HouseDTO houseDTO = JSON.parseObject(map.get(HouseConstant.update), HouseDTO.class);
            HouseIndex houseIndex = new HouseIndex();
            BeanUtils.copyProperties(houseDTO, houseIndex);
            houseIndex.setId(Long.valueOf(houseDTO.getId()));
            houseIndex.setHouseId(Long.valueOf(houseDTO.getId()));

            BeanUtils.copyProperties(houseDTO.getHouseDetail(), houseIndex);

            StringBuffer tags = new StringBuffer();
            for (String tag : houseDTO.getTags()){
                tags.append(tags+",");
            }
            houseIndex.setTags(tags.toString().substring(0, tags.length()-1));
            houseSearchDao.deleteById(houseIndex.getId());
            houseSearchDao.save(houseIndex);
        }
        if (StringUtils.isNotBlank(map.get(HouseConstant.delete))){

            HouseDTO houseDTO = JSON.parseObject(map.get(HouseConstant.delete), HouseDTO.class);
            houseSearchDao.deleteById(Long.valueOf(houseDTO.getId()));
        }
    }

}
