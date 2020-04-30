package com.zxf;

import com.alibaba.fastjson.JSON;
import com.zxf.VO.RentHouseSearchVo;
import com.zxf.result.PageResult;
import com.zxf.service.HouseSearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ESTest {

    @Autowired
    HouseSearchService houseSearchService;

//    @Test
//    public void ESt(){
//        RentHouseSearchVo searchVo = new RentHouseSearchVo();
////        searchVo.setKeywords("望春园板楼三居室 自住精装 南北通透 采光好视野棒！");
//        searchVo.setCityEnName("bj");
//        PageResult houseDTO = houseSearchService.searchHouses(searchVo);
//        System.out.println(JSON.toJSONString(houseDTO));
//    }

}
