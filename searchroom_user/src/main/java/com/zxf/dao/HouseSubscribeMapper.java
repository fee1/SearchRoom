package com.zxf.dao;

import com.zxf.DO.HouseSubscribe;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@Mapper
@Repository
public interface HouseSubscribeMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table house_subscribe
     *
     * @mbggenerated Fri Apr 03 15:49:55 CST 2020
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table house_subscribe
     *
     * @mbggenerated Fri Apr 03 15:49:55 CST 2020
     */
    int insert(HouseSubscribe record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table house_subscribe
     *
     * @mbggenerated Fri Apr 03 15:49:55 CST 2020
     */
    HouseSubscribe selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table house_subscribe
     *
     * @mbggenerated Fri Apr 03 15:49:55 CST 2020
     */
    List<HouseSubscribe> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table house_subscribe
     *
     * @mbggenerated Fri Apr 03 15:49:55 CST 2020
     */
    int updateByPrimaryKey(HouseSubscribe record);

    int insertSelective(HouseSubscribe subscribe);

    int deleteSubscribe(@Param("userId") String userId,@Param("houseId") String houseId);

    HouseSubscribe getSubscribe(@Param("houseId") String houseId,@Param("userId") String userId);

    List<HouseSubscribe> subscribeList(@Param("userId") String userId,@RequestParam("status") Integer status);

    int reservation(@Param("userId") String userId, @Param("houseId") String houseId, @Param("phone") String phone, @Param("orderTime") String orderTime, @Param("updateTime")String updateTime);

    List<HouseSubscribe> adminSubscribeList(String adminId);

    int finshSubscribe(@Param("houseId") String houseId,@Param("userId") String userId, @Param("updateTime") Date updateTime);
}