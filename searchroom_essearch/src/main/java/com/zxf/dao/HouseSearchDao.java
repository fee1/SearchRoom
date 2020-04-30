package com.zxf.dao;

import com.zxf.pojo.HouseIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HouseSearchDao extends ElasticsearchCrudRepository<HouseIndex, Long>, ElasticsearchRepository<HouseIndex, Long> {



}
