package com.qingcheng.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.qingcheng.service.goods.SkuSearchService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.naming.directory.SearchResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@Service
public class SkuSearchServiceImpl implements SkuSearchService {

    @Autowired
    protected RestHighLevelClient restHighLevelClient;

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

        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        Map resultMap = new HashMap();
        ArrayList resultList = new ArrayList<Map<String, Object>>();

        SearchHits searchHits = searchResponse.getHits();
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits) {
            Map<String, Object> skuMap = hit.getSourceAsMap();
            resultList.add(skuMap);
        }

        resultMap.put("list", resultList);
        return resultMap;
    }
}
