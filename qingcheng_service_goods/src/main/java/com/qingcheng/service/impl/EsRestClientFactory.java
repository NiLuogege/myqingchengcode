package com.qingcheng.service.impl;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * es 客户端 工厂
 */
public class EsRestClientFactory {

    public static RestHighLevelClient getRestHighLevelClient(String hostName, int part){
        HttpHost http = new HttpHost(hostName,part,"http");
        RestClientBuilder builder = RestClient.builder(http);
        return new RestHighLevelClient(builder);

    }

}
