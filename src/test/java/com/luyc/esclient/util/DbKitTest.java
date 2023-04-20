package com.luyc.esclient.util;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.AggregationBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQueryField;
import co.elastic.clients.elasticsearch.cat.indices.IndicesRecord;
import co.elastic.clients.json.JsonData;
import com.luyc.esclient.EsClientApplicationTests;
import com.luyc.esclient.UIMRScanRecord;
import com.luyc.esclient.common.AggQuery;
import com.luyc.esclient.common.AggResult;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
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
    }

    @Test
    void selSingleDocument() throws IOException {
        List<UIMRScanRecord> person = dbKit.selList10000("xczk_ui_mr_scan_record_collector",null ,null, UIMRScanRecord.class);
    }


    @Test
    void insert() throws IOException {
//        Person person = new Person();
//        person.setName("sadwd");
//        person.setName("has id");
//        person.setBirthday(LocalDate.now());
//        person.setHeight(185);
//        person.setMoto("hahahaha");
//        person.setNum("148468");
//        dbKit.save("person_t",person);
    }

    @Test
    void selByAggs() throws IOException {
        BoolQuery.Builder builder = new BoolQuery.Builder();

        List<FieldValue> list = new ArrayList<>();
        list.add(FieldValue.of(0));
        list.add(FieldValue.of(3));
        list.add(FieldValue.of(4));
        list.add(FieldValue.of(5));
        list.add(FieldValue.of(7));
        TermsQueryField values= TermsQueryField.of(b->b.value(list));

        builder.filter(b->b.range(s->s.field("start_time").gte(JsonData.of("2022-12-01 00:00:00")).lte(JsonData.of("2022-12-31 23:59:59"))));
        builder.filter(b->b.terms(t->t.field("hosp_id").terms(values)));

        BoolQuery  boolQuery = builder.build();
        Query query = new Query(boolQuery);

        Aggregation sum = AggregationBuilders.sum(s->s.field("useTimes"));

        Aggregation.Builder aggBuilder = new Aggregation.Builder();
        Aggregation agg = aggBuilder.terms(t->t.field("type")).aggregations("sum",sum).aggregations("hosp",AggregationBuilders.terms(s->s.field("hosp_id"))).build();
        AggResult result =  dbKit.selByAgg("xczk_usetime_pacs",query,new AggQuery("type",agg));
    }
}
