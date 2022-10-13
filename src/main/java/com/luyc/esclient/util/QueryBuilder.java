package com.luyc.esclient.util;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQueryField;
import co.elastic.clients.json.JsonData;
import com.luyc.esclient.common.FieldQuery;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author luyc
 * @since 2022/10/13 17:15
 */
public class QueryBuilder {

    public enum BoolType {
        MATCH,//分词匹配
        MATCH_PHRASE,//精确匹配

        ISNUll,//为空
        ISNotNUll,//不为空

        TERM_STR,//精确匹配 字符串
        TERM_LONG,//精确匹配 长整型
        TERM_BOOL,//精确匹配 波尔
        TERM_DOUBLE,//精确匹配 浮点数

        TERMIN_STR,//在....之内


        RANGEGT,//大于
        RANGELT,//小于
        RANGEGTE,//大于等于
        RANGELTE,//小于等于
        WILDCARD//模糊匹配
        ;
    }


    /**
     * @return co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery
     * @Author luyc
     * @Description 创建bool查询条件
     * @Date 2022/10/13 17:14
     * @Param [list]
     **/
    public static BoolQuery buildQuery(List<FieldQuery> list) {
        BoolQuery boolQuery = BoolQuery.of(bool -> {

            return bool;
        });
        return boolQuery;
    }

    /**
     * @return void
     * @Author luyc
     * @Description 生成bool查询的条件
     * @Date 2022/10/13 17:13
     * @Param [builder, fieldQuery]
     **/
    private static void query(BoolQuery.Builder builder, FieldQuery fieldQuery) {
        if (fieldQuery.getType().equals(BoolType.TERM_STR)) {
            builder.filter(filter ->
                    filter.term(t ->
                            t.field(fieldQuery.getField()).value((String) fieldQuery.getValue())
                    )
            );
        } else if (fieldQuery.getType().equals(BoolType.TERM_LONG)) {
            builder.filter(filter ->
                    filter.term(t ->
                            t.field(fieldQuery.getField()).value((Long) fieldQuery.getValue())
                    )
            );
        } else if (fieldQuery.getType().equals(BoolType.TERM_BOOL)) {
            builder.filter(filter ->
                    filter.term(t ->
                            t.field(fieldQuery.getField()).value((Boolean) fieldQuery.getValue())
                    )
            );
        } else if (fieldQuery.getType().equals(BoolType.TERM_DOUBLE)) {
            builder.filter(filter ->
                    filter.term(t ->
                            t.field(fieldQuery.getField()).value((Double) fieldQuery.getValue())
                    )
            );
        } else if (fieldQuery.getType().equals(BoolType.TERM_STR)) {
            List<FieldValue> values = new ArrayList<>();
            Iterator<String> iterator = ((List<String>) fieldQuery.getValue()).iterator();
            while (iterator.hasNext()) {
                values.add(FieldValue.of(iterator.next()));
            }
            builder.filter(filter ->
                    filter.terms(t ->
                            t.field(fieldQuery.getField()).terms(TermsQueryField.of(b -> b.value(values)))
                    )
            );
        } else if (fieldQuery.getType().equals(BoolType.RANGEGTE)) {
            builder.filter(filter ->
                    filter.range(r ->
                            r.field(fieldQuery.getField()).gte(JsonData.of(fieldQuery.getValue()))
                    )
            );
        } else if (fieldQuery.getType().equals(BoolType.RANGEGT)) {
            builder.filter(filter ->
                    filter.range(r ->
                            r.field(fieldQuery.getField()).gt(JsonData.of(fieldQuery.getValue()))
                    )
            );
        } else if (fieldQuery.getType().equals(BoolType.RANGELTE)) {
            builder.filter(filter ->
                    filter.range(r ->
                            r.field(fieldQuery.getField()).lte(JsonData.of(fieldQuery.getValue()))
                    )
            );
        } else if (fieldQuery.getType().equals(BoolType.RANGELT)) {
            builder.filter(filter ->
                    filter.range(r ->
                            r.field(fieldQuery.getField()).lt(JsonData.of(fieldQuery.getValue()))
                    )
            );
        } else if (fieldQuery.getType().equals(BoolType.WILDCARD)) {
            StringBuilder sb = new StringBuilder();
            sb.append("*").append(fieldQuery.getValue()).append("*");
            builder.must(must ->
                    must.wildcard(w ->
                            w.field(fieldQuery.getField()).value(sb.toString())
                    )
            );
        }
    }
}
