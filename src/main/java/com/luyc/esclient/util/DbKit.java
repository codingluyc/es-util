package com.luyc.esclient.util;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch.cat.IndicesRequest;
import co.elastic.clients.elasticsearch.cat.indices.IndicesRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * es工具类
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
     * @Author luyc
     * @Description 查询所有索引
     * @Date 2022/10/11 17:28
     * @Param []
     * @return java.util.List<co.elastic.clients.elasticsearch.cat.indices.IndicesRecord>
     **/
    public List<IndicesRecord> selIndices() throws IOException {
        List<IndicesRecord> records =  client.cat().indices().valueBody();
        return records;
    }


}
