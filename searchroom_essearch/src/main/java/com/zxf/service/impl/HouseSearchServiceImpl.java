package com.zxf.service.impl;

import com.zxf.Constant;
import com.zxf.DTO.HouseBucketDTO;
import com.zxf.DTO.HouseDTO;
import com.zxf.DTO.HouseDetailDTO;
import com.zxf.DTO.RentValueBlockDTO;
import com.zxf.VO.MapSearchVo;
import com.zxf.VO.RentHouseSearchVo;
import com.zxf.HouseConstant;
import com.zxf.dao.HouseSearchDao;
import com.zxf.pojo.HouseIndex;
import com.zxf.result.PageResult;
import com.zxf.service.HouseSearchService;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
public class HouseSearchServiceImpl implements HouseSearchService {

    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    HouseSearchDao houseSearchDao;

    /**
     * 房源信息搜索
     * @param searchVo
     * @return
     */
    @Override
    public PageResult searchHouses(RentHouseSearchVo searchVo) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //必须匹配字段，不分词匹配
        boolQueryBuilder.filter(QueryBuilders.termQuery(HouseConstant.cityEnName, searchVo.getCityEnName()));

        if (StringUtils.isNotBlank(searchVo.getRegionEnName()) && !StringUtils.equals(searchVo.getRegionEnName(), "*")){
            boolQueryBuilder.filter(QueryBuilders.termQuery(HouseConstant.regionEnName, searchVo.getRegionEnName()));
        }
        boolQueryBuilder.filter(QueryBuilders.termQuery(HouseConstant.status, 1));

        //范围查询
        RentValueBlockDTO price = RentValueBlockDTO.matchPrice(searchVo.getPriceBlock());
        if (!price.equals(RentValueBlockDTO.ALL)){
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(HouseConstant.price);
            if (price.getMin() > 0){
                rangeQueryBuilder.gte(price.getMin());
            }
            if (price.getMax() > 0){
                rangeQueryBuilder.lte(price.getMax());
            }
            boolQueryBuilder.filter(rangeQueryBuilder);
        }
        RentValueBlockDTO area = RentValueBlockDTO.matchArea(searchVo.getAreaBlock());
        if (!area.equals(RentValueBlockDTO.ALL)){
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(HouseConstant.area);
            if (area.getMin() > 0){
                rangeQueryBuilder.gte(area.getMin());
            }
            if (area.getMax() > 0){
                rangeQueryBuilder.lte(area.getMax());
            }
            boolQueryBuilder.filter(rangeQueryBuilder);
        }
        //
        if (searchVo.getDirection() > 0){
            boolQueryBuilder.filter(QueryBuilders.termQuery(HouseConstant.direction, searchVo.getDirection()));
        }
        if (searchVo.getRoom() > 0){
            boolQueryBuilder.filter(QueryBuilders.termQuery(HouseConstant.room, searchVo.getRoom()));
        }
        if (searchVo.getRentWay() > -1){
            boolQueryBuilder.filter(QueryBuilders.termQuery(HouseConstant.rentWay, searchVo.getRentWay()));
        }

        //关键字搜索匹配, 分词
        MultiMatchQueryBuilder searchQueryBuilder = null;
        if (StringUtils.isNotBlank(searchVo.getKeywords())) {
            ////////////关键词搜索优化，title权重增加
            searchQueryBuilder = QueryBuilders.multiMatchQuery(searchVo.getKeywords(),HouseConstant.title,HouseConstant.district,
                    HouseConstant.street, HouseConstant.description, HouseConstant.layoutDesc);
        }

        //排序 那一个字段  升降序
        SortBuilder sortBuilder = SortBuilders.fieldSort(searchVo.getOrderBy()).order(SortOrder.fromString(searchVo.getOrderDirection()));

        //分页
        PageRequest pageRequest = PageRequest.of(searchVo.getStart() / searchVo.getSize(), searchVo.getSize());
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withFilter(boolQueryBuilder)
                .withQuery(searchQueryBuilder)
                .withSort(sortBuilder)
                .withPageable(pageRequest).build();
//        Page<HouseIndex> page = elasticsearchTemplate.queryForPage(searchQuery, HouseIndex.class);
        Page<HouseIndex> page = null;
        try {
            page = houseSearchDao.search(searchQuery);
        }catch (Exception e){
            e.printStackTrace();
            return new PageResult();
        }

        List<HouseDTO> houseDTOS = this.wrapperHouseSearch(page);

        PageResult pageResult = new PageResult();
        pageResult.setTotal(Integer.parseInt(Long.toString(page.getTotalElements())));
        pageResult.setResult(houseDTOS);
        return pageResult;
    }

    /**
     * 地图房源信息聚合搜索   查询每个区域的房源数量
     * @param cityEnName
     */
    @Override
    public PageResult mapAggregation(String cityEnName) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.filter(QueryBuilders.termQuery(HouseConstant.cityEnName, cityEnName));
        boolQueryBuilder.filter(QueryBuilders.termQuery(HouseConstant.status, 1));

        //放到bucket名字为HouseConstant.agg_region
        AbstractAggregationBuilder aggregationBuilder = AggregationBuilders.terms(HouseConstant.agg_region).field(HouseConstant.regionEnName);

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withFilter(boolQueryBuilder)
                .addAggregation(aggregationBuilder).build();

        SearchResponse searchResponse = null;

        try {
            searchResponse = elasticsearchTemplate.query(searchQuery, new ResultsExtractor<SearchResponse>() {
                @Override
                public SearchResponse extract(SearchResponse searchResponse) {
                    return searchResponse;
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            return new PageResult();
        }

        List<HouseBucketDTO> bucketDTOS = new ArrayList<>();
        Terms terms = searchResponse.getAggregations().get(HouseConstant.agg_region);
        for (Terms.Bucket bucket : terms.getBuckets()){
            bucketDTOS.add(new HouseBucketDTO(bucket.getKeyAsString(), bucket.getDocCount()));
        }

        PageResult result = new PageResult();
        result.setTotal(Integer.valueOf(Long.toString(searchResponse.getHits().totalHits)));
        result.setResult(bucketDTOS);
        return result;
    }

    /**
     * 地图精确查询house数据
     * @param searchVo
     * @return
     */
    @Override
    public PageResult mapHouses(MapSearchVo searchVo) {
        if (searchVo.getLevel() < 13){
            //查询全部house数据
            return this.allCityHouses(searchVo);
        }else {
            //地图位置动态查询房子
            return this.reginDynamicHouses(searchVo);
        }
    }

    /**
     * 根据地图动态查找房源数据
     * @param searchVo
     * @return
     */
    private PageResult reginDynamicHouses(MapSearchVo searchVo) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.filter(QueryBuilders.termQuery(HouseConstant.cityEnName, searchVo.getCityEnName()));
        boolQueryBuilder.filter(QueryBuilders.termQuery(HouseConstant.status, 1));
        boolQueryBuilder.filter(QueryBuilders.geoBoundingBoxQuery(HouseConstant.location).setCorners(
                new GeoPoint(searchVo.getLeftLatitude(), searchVo.getLeftLongitude()),
                new GeoPoint(searchVo.getRightLatitude(), searchVo.getRightLongitude())
        ));

        SortBuilder sortBuilder = SortBuilders.fieldSort(searchVo.getOrderBy()).order(SortOrder.fromString(searchVo.getOrderDirection()));

        PageRequest pageRequest = PageRequest.of(searchVo.getStart() / searchVo.getSize(), searchVo.getSize());
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withFilter(boolQueryBuilder)
                .withSort(sortBuilder)
                .withPageable(pageRequest).build();
        Page<HouseIndex> page =null;
        try {
            page = houseSearchDao.search(searchQuery);
        }catch (Exception e){
            e.printStackTrace();
            return new PageResult();
        }

        List<HouseDTO> houseDTOS = wrapperHouseSearch(page);

        PageResult result = new PageResult();
        result.setResult(houseDTOS);
        result.setTotal(Integer.valueOf(Long.toString(page.getTotalElements())));
        return result;
    }

    /**
     * 整个城市房源数据
     * @return
     */
    public PageResult allCityHouses(MapSearchVo searchVo){
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.filter(QueryBuilders.termQuery(HouseConstant.cityEnName, searchVo.getCityEnName()));
        boolQueryBuilder.filter(QueryBuilders.termQuery(HouseConstant.status, 1));

        SortBuilder sortBuilder = SortBuilders.fieldSort(searchVo.getOrderBy()).order(SortOrder.fromString(searchVo.getOrderDirection()));

        PageRequest pageRequest = PageRequest.of(searchVo.getStart() / searchVo.getSize(), searchVo.getSize());
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withFilter(boolQueryBuilder)
                .withSort(sortBuilder)
                .withPageable(pageRequest).build();
        Page<HouseIndex> page = null;

        try {
            page = houseSearchDao.search(searchQuery);
        }catch (Exception e){
            e.printStackTrace();
            return new PageResult();
        }

        List<HouseDTO> houseDTOS = this.wrapperHouseSearch(page);

        PageResult result = new PageResult();
        result.setTotal(Integer.valueOf(Long.toString(page.getTotalElements())));
        result.setResult(houseDTOS);
        return result;
    }

    public List<HouseDTO> wrapperHouseSearch(Page<HouseIndex> page){
        List<HouseDTO> houseDTOS = new ArrayList<>();
        for (HouseIndex index : page.getContent()){
            HouseDTO houseDTO = new HouseDTO();
            BeanUtils.copyProperties(index, houseDTO);
            houseDTO.setId(Integer.valueOf(index.getId().toString()));
            HouseDetailDTO houseDetailDTO = new HouseDetailDTO();
            BeanUtils.copyProperties(index, houseDetailDTO);
            houseDTO.setHouseDetail(houseDetailDTO);
            houseDTO.setTags(Arrays.asList(index.getTags()));
            houseDTO.setCover(Constant.aliImage+houseDTO.getCover());
            houseDTOS.add(houseDTO);
        }
        return houseDTOS;
    }

}
