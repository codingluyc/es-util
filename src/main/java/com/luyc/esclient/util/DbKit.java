package com.luyc.esclient.util;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch.cat.IndicesRequest;
import co.elastic.clients.elasticsearch.cat.indices.IndicesRecord;
import co.elastic.clients.elasticsearch.core.GetRequest;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.indices.GetIndexRequest;
import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import co.elastic.clients.elasticsearch.indices.IndexState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * es工具类
 *
 * @author luyc
 * @since 2022/10/11 16:43
 */
@Component
public class DbKit {
    private ElasticsearchClient client;

    @Autowired
    public void setClient(ElasticsearchClient client) {
        this.client = client;
    }

    /**
     * @return java.util.List<co.elastic.clients.elasticsearch.cat.indices.IndicesRecord>
     * @Author luyc
     * @Description 查询所有索引
     * @Date 2022/10/11 17:28
     * @Param []
     **/
    public List<IndicesRecord> selIndices() throws IOException {
        List<IndicesRecord> records = client.cat().indices().valueBody();
        return records;
    }

    public TypeMapping selFields(String index) throws IOException {
        GetIndexResponse response =client.indices().get(GetIndexRequest.of(builder -> {
            List<String>  list = new ArrayList<>();
            builder.index(index);
            return builder;
        }));
        IndexState result = response.get(index);
        return result.mappings();
    }


}
