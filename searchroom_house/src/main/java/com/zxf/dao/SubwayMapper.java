package com.zxf.dao;

import com.zxf.DO.Subway;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface SubwayMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table subway
     *
     * @mbggenerated Fri Apr 03 15:49:55 CST 2020
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table subway
     *
     * @mbggenerated Fri Apr 03 15:49:55 CST 2020
     */
    int insert(Subway record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table subway
     *
     * @mbggenerated Fri Apr 03 15:49:55 CST 2020
     */
    Subway selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table subway
     *
     * @mbggenerated Fri Apr 03 15:49:55 CST 2020
     */
    List<Subway> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table subway
     *
     * @mbggenerated Fri Apr 03 15:49:55 CST 2020
     */
    int updateByPrimaryKey(Subway record);

    List<Subway> selectByCityName(String cityName);
}