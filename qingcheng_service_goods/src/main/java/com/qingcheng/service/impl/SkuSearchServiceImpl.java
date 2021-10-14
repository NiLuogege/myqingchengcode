package com.qingcheng.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.qingcheng.dao.BrandMapper;
import com.qingcheng.service.goods.SkuSearchService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.naming.directory.SearchResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Service
public class SkuSearchServiceImpl implements SkuSearchService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private BrandMapper brandMapper;

    @Override
    public Map search(Map<String, String> searchMap) throws IOException {

        //封装查询请求
        SearchRequest searchRequest = new SearchRequest("sku");
        searchRequest.types("doc");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //布尔查询构建器(相当于搜索方式（精确匹配，模糊查询，筛选）的连接器)
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        String keywords = searchMap.get("keywords");
        if (!StringUtils.isEmpty(keywords)) {
            MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("name", keywords);
            boolQueryBuilder.must(matchQueryBuilder);
        }

        //通过分类过滤查询
        String category = searchMap.get("category");
        if (!StringUtils.isEmpty(category)){
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("categoryName", category);
            boolQueryBuilder.filter(termQueryBuilder);
        }

        //通过品牌过滤查询
        String brand = searchMap.get("brand");
        if (!StringUtils.isEmpty(brand)){
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("brandName", brand);
            boolQueryBuilder.filter(termQueryBuilder);
        }


        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);


        //聚合查询（商品分类）目的是将所有查询出来的 categoryName 进行聚合
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("sku_category").field("categoryName");
        searchSourceBuilder.aggregation(termsAggregationBuilder);


        searchSourceBuilder.from(0);//开始索引设置
        searchSourceBuilder.size(200);//每页记录数设置

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        Map resultMap = new HashMap();

        //2.1 商品列表
        ArrayList resultList = new ArrayList<Map<String, Object>>();
        SearchHits searchHits = searchResponse.getHits();
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits) {
            Map<String, Object> skuMap = hit.getSourceAsMap();
            resultList.add(skuMap);
        }
        resultMap.put("list", resultList);

        //2.2 商品分类列表
        Aggregations aggregations = searchResponse.getAggregations();
        Map<String, Aggregation> aggregationMap = aggregations.getAsMap();
        Terms terms = (Terms) aggregationMap.get("sku_category");
        List<? extends Terms.Bucket> buckets = terms.getBuckets();
        ArrayList<String> categoryList = new ArrayList<>();
        for (Terms.Bucket bucket : buckets) {
            categoryList.add(bucket.getKeyAsString());
        }
        resultMap.put("categoryList", categoryList);


        //2.3 品牌列表
        if (StringUtils.isEmpty(category) && categoryList.size()>0){
            category=categoryList.get(0);
        }
        if (!StringUtils.isEmpty(category)){
            List<Map> brandList = brandMapper.findListByCategoryName(category);
            resultMap.put("brandList",brandList);
        }

        return resultMap;
    }
}
