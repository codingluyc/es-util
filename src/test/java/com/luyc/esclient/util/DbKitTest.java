package com.luyc.esclient.util;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.cat.indices.IndicesRecord;
import co.elastic.clients.json.JsonData;
import com.luyc.esclient.EsClientApplicationTests;
import com.luyc.esclient.po.Person;
import com.luyc.esclient.vo.Field;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

/**
 * @author luyc
 * @since 2022/10/11 17:26
 */
public class DbKitTest extends EsClientApplicationTests {


    /**
     * 测试查询索引列表
     * @throws IOException
     */
    @Test
    public void testIndexList() throws IOException {
        List<IndicesRecord> list = dbKit.indices("person_*");
//        List<Field> properties = dbKit.selFields("person_t");
    }

    @Test
    void selSingleDocument() throws IOException {
        BoolQuery boolQuery = BoolQuery.of(bool->
            bool.filter(filter->
                filter.range(r->
                    r.field("birthday").gte(JsonData.of("1998-09-08"))
                )
            )
        );

        Query query = Query.of(q->q.bool(boolQuery));
        Person person = dbKit.selSingleDocument("person_t",query ,Person.class);

    }
}
