package com.luyc.esclient.util;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.cat.IndicesRequest;
import co.elastic.clients.elasticsearch.cat.indices.IndicesRecord;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.GetIndexRequest;
import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import co.elastic.clients.elasticsearch.indices.IndexState;
import com.luyc.esclient.common.AggQuery;
import com.luyc.esclient.common.AggResult;
import com.luyc.esclient.common.BaseEntity;
import com.luyc.esclient.common.OrderQuery;
import com.luyc.esclient.vo.Field;
import org.elasticsearch.client.RequestOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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




//    public static final int MATCH = 1;//分词匹配
//    public static final int MATCH_PHRASE = 2;//精确匹配
//
//    public static final int ISNUll = 3;//为空
//    public static final int ISNotNUll = 4;//不为空
//
//    public static final int TERM = 5;//精确匹配
//    public static final int NOTTERM = 6;//不相等
//    public static final int TERMIN = 7;//在....之内
//
//    public static final int RANGEGT = 8;//大于
//    public static final int RANGELT = 9;//小于
//    public static final int RANGEGTE = 10;//大于等于
//    public static final int RANGELTE = 11;//小于等于
//
//
//    public static final int WILDCARD = 12;//模糊匹配




    //默认最大数据量
    public static int MAX_SIZE = 10000;

    /**
     * @Author luyc
     * @Description 创建排序
     * @Date 2022/10/14 10:14
     * @Param [sorts]
     * @return java.util.List<co.elastic.clients.elasticsearch._types.SortOptions>
     **/
    private List<SortOptions> sort(List<OrderQuery> sorts){
        if(!CollectionUtils.isEmpty(sorts)) {
            return sorts.stream().map(s -> s.toSort()).collect(Collectors.toList());
        }else{
            return new ArrayList<>();
        }
    }

    /**
     * @Author luyc
     * @Description 创建文档查询请求
     * @Date 2022/10/14 10:18
     * @Param [index, query, sorts, size, queryAll]
     * @return co.elastic.clients.elasticsearch.core.SearchRequest
     **/
    private SearchRequest search(String index, Query query, List<OrderQuery> sorts, Integer size, boolean queryAll){
        SearchRequest searchRequest = SearchRequest.of(builder -> {
            builder.index(index);
            builder.query(query);
            if(queryAll) {
                //track_total_hits": true
                builder.trackTotalHits(t -> t.enabled(true));
            }
            if(!CollectionUtils.isEmpty(sorts)) {
                builder.sort(sort(sorts));
            }
            if(size != null && size.intValue() >= 0){
                builder.size(size);
            }
            return builder;
        });
        return searchRequest;
    }

    /**
     * @Author luyc
     * @Description 创建文档查询请求
     * @Date 2022/10/14 10:18
     * @Param [index, query, sorts, size, queryAll]
     * @return co.elastic.clients.elasticsearch.core.SearchRequest
     **/
    private SearchRequest search(String index, Query query, List<OrderQuery> sorts, Integer size, boolean queryAll, List<AggQuery> aggregations){
        SearchRequest searchRequest = SearchRequest.of(builder -> {
            builder.index(index);
            builder.query(query);
            if(queryAll) {
                //track_total_hits": true
                builder.trackTotalHits(t -> t.enabled(true));
            }
            if(!CollectionUtils.isEmpty(sorts)) {
                builder.sort(sort(sorts));
            }
            if(size != null && size.intValue() >= 0){
                builder.size(size);
            }
            if(!CollectionUtils.isEmpty(aggregations)) {
                Map<String,Aggregation> aggregationMap = new HashMap<>();
                for(AggQuery aggregation:aggregations){
                    aggregationMap.put(aggregation.getAggName(),aggregation.getAggregation());
                }
                builder.aggregations(aggregationMap);
            }
            return builder;
        });
        return searchRequest;
    }

    /**
     * @Author luyc
     * @Description 创建文档查询请求
     * @Date 2022/10/14 10:18
     * @Param [index, query, sorts, size, queryAll]
     * @return co.elastic.clients.elasticsearch.core.SearchRequest
     **/
    private SearchRequest search(String index, Query query, List<OrderQuery> sorts, Integer size, boolean queryAll, AggQuery aggregation){
        SearchRequest searchRequest = SearchRequest.of(builder -> {
            builder.index(index);
            builder.query(query);
            if(queryAll) {
                //track_total_hits": true
                builder.trackTotalHits(t -> t.enabled(true));
            }
            if(!CollectionUtils.isEmpty(sorts)) {
                builder.sort(sort(sorts));
            }
            if(size != null && size.intValue() >= 0){
                builder.size(size);
            }
            if(aggregation != null){
                builder.aggregations(aggregation.getAggName(),aggregation.getAggregation());
            }
            return builder;
        });
        return searchRequest;
    }

    /**
     * @return java.util.List<co.elastic.clients.elasticsearch.cat.indices.IndicesRecord>
     * @Author luyc
     * @Description 查询所有索引
     * @Date 2022/10/11 17:28
     * @Param []
     **/
    public List<IndicesRecord> indices(String index) throws IOException {
        IndicesRequest request = IndicesRequest.of(builder -> {
            if(StringUtils.hasLength(index)) {
                builder.index(index);
            }
            return builder;
        });
        List<IndicesRecord> records = client.cat().indices(request).valueBody();
        return records;
    }

    /**
     * @Author luyc
     * @Description 查询索引的字段结构
     * @Date 2022/10/13 15:33
     * @Param [index]
     * @return co.elastic.clients.elasticsearch._types.mapping.TypeMapping
     **/
    public List<Field> selFields(String index) throws IOException {
        GetIndexResponse response =client.indices().get(GetIndexRequest.of(builder -> {
            List<String>  list = new ArrayList<>();
            builder.index(index);
            return builder;
        }));
        IndexState result = response.get(index);
        TypeMapping mapping = result.mappings();
        Map<String, Property> fields =  mapping.properties();
        Iterator<Map.Entry<String,Property>> iterator = fields.entrySet().iterator();
        List<Field> fieldList = new ArrayList<>();
        while(iterator.hasNext()){
            Map.Entry<String,Property> entry = iterator.next();
            String fieldName = entry.getKey();
            Property property = entry.getValue();
            fieldList.add(new Field(fieldName, property));
        }
        return fieldList;
    }

    /**
     * @Author luyc
     * @Description 查询单个文档
     * @Date 2022/10/13 16:10
     * @Param [index, query, documentType]
     * @return T
     **/
    public <T> T selSingleDocument(String index,Query query,Class<T> documentType) throws IOException {
        SearchRequest searchRequest = search(index,query,null,1,false);
        SearchResponse<T> response =  client.search(searchRequest,documentType);
        List<Hit<T>> list = response.hits().hits();
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        T result = list.get(0).source();
        if(BaseEntity.class.isAssignableFrom(documentType)){
            BaseEntity baseEntity = (BaseEntity)result;
            baseEntity.setId(list.get(0).id());
        }
        return result;
    }

    
    /**
     * @Author luyc
     * @Description 查询列表
     * @Date 2022/10/14 10:21
     * @Param [index, query, sorts, documentType]
     * @return java.util.List<T>
     **/
    public <T> List<T> selList(String index, Query query, List<OrderQuery> sorts,Class<T> documentType) throws IOException {
        SearchRequest searchRequest = search(index,query,sorts,null,true);
        SearchResponse<T> response =  client.search(searchRequest,documentType);
        List<Hit<T>> hits = response.hits().hits();
        List<T> list = new ArrayList<>(hits.size());
        Iterator<Hit<T>> iterator = hits.iterator();
        while(iterator.hasNext()){
            Hit<T> hit = iterator.next();
            T t = hit.source();
            if(BaseEntity.class.isAssignableFrom(documentType)){
                BaseEntity baseEntity = (BaseEntity)t;
                baseEntity.setId(hit.id());
            }
            list.add(t);
        }
        return list;
    }

    /**
     * @author luyc
     * @Description 聚合查询
     * @Date 2022/12/7 12:18
     * @param index
     * @param query
     * @param aggQuery
     * @return physical.common.pojo.AggResult
     **/
    public AggResult selByAgg(String index,  Query query, AggQuery aggQuery) throws IOException {
        SearchRequest searchRequest = search(index,query,null,0,true,aggQuery);

        SearchResponse searchResponse =  client.search(searchRequest,null);
        Map<String, Aggregate> agg = searchResponse.aggregations();
        AggResult aggResult = new AggResult(aggQuery.getAggName());
        agg(agg.get(aggQuery.getAggName()),aggResult);
        return aggResult;
    }

    /**
     * @author luyc
     * @Description 多聚合查询
     * @Date 2022/12/22 10:45
     * @param index
     * @param boolQueryBuilder
     * @param aggs
     * @return java.util.List<physical.common.pojo.AggResult>
     **/
    public List<AggResult> selByAggs(String index, BoolQueryBuilder boolQueryBuilder,AggregationBuilder... aggs) throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(boolQueryBuilder);
        sourceBuilder.size(0);
        for(AggregationBuilder aggregationBuilder:aggs) {
            sourceBuilder.aggregation(aggregationBuilder);
        }

        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse =  client.search(searchRequest, RequestOptions.DEFAULT);
        List<AggResult> list = new ArrayList<>();
        for(AggregationBuilder aggregationBuilder:aggs) {
            Aggregation agg = searchResponse.getAggregations().get(aggregationBuilder.getName());
            AggResult aggResult = new AggResult(agg.getName());
            agg(agg,aggResult);
            list.add(aggResult);
        }
        return list;
    }

    /**
     * @author luyc
     * @Description 使用递归的方式处理多层聚合
     * @Date 2022/12/1 11:34
     * @param agg 聚合
     * @param result agg这层聚合的值
     * @return physical.common.Record
     **/
    private AggResult agg(Aggregate agg,AggResult result){
        if(agg.isMultiTerms() ){
            result.initBucket();
            for(){
                Bucket b = new Bucket(bucket.getKeyAsString(),bucket.getDocCount());
                result.addBucket(b);
                b.initSubAggs();
                List<Aggregation> aggregations = bucket.getAggregations().asList();
                for(Aggregation tmp:aggregations){
                    AggResult r = new AggResult(tmp.getName());
                    agg(tmp,r);
                    b.addSubAgg(r);
                }
            }
        }else if (agg instanceof ParsedDateHistogram) {
            ParsedDateHistogram parsedDateHistogram = (ParsedDateHistogram) agg;
            result.initBucket();
            for(Histogram.Bucket  dateBucket:parsedDateHistogram.getBuckets()){
                Bucket b = new Bucket(dateBucket.getKeyAsString(), dateBucket.getDocCount());
                result.addBucket(b);
                b.initSubAggs();
                List<Aggregation> aggregations = dateBucket.getAggregations().asList();
                for(Aggregation tmp:aggregations){
                    AggResult r = new AggResult(tmp.getName());
                    agg(tmp,r);
                    b.addSubAgg(r);
                }
            }
        }else{
            if(agg instanceof  ParsedSum) {
                ParsedSum sum = (ParsedSum) agg;
                result.setValue(sum.getValueAsString());
            }else if(agg instanceof  ParsedValueCount) {
                ParsedValueCount count = (ParsedValueCount) agg;
                result.setValue(count.getValueAsString());
            } else if(agg instanceof ParsedCardinality){
                ParsedCardinality cardinality = (ParsedCardinality) agg;
                result.setValue(cardinality.getValueAsString());
            } else if(agg instanceof ParsedAvg){
                ParsedAvg parsedAggregation = (ParsedAvg) agg;
                result.setValue(parsedAggregation.getValueAsString());
            }
        }
        return result;
    }

}
