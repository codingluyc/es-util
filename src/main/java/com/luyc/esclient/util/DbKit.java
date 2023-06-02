package com.luyc.esclient.util;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Refresh;
import co.elastic.clients.elasticsearch._types.Result;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.aggregations.*;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.cat.IndicesRequest;
import co.elastic.clients.elasticsearch.cat.indices.IndicesRecord;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.SourceConfig;
import co.elastic.clients.elasticsearch.indices.GetIndexRequest;
import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import co.elastic.clients.elasticsearch.indices.IndexState;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.luyc.esclient.common.*;
import com.luyc.esclient.vo.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;
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
    private static final Logger log = LoggerFactory.getLogger(DbKit.class);

    private ElasticsearchClient client;

    @Autowired
    public void setClient(ElasticsearchClient client) {
        this.client = client;
    }


    //默认最大数据量
    public static int MAX_SIZE = 10000;

    /**
     * @return java.util.List<co.elastic.clients.elasticsearch._types.SortOptions>
     * @Author luyc
     * @Description 创建排序
     * @Date 2022/10/14 10:14
     * @Param [sorts]
     **/
    private List<SortOptions> sort(List<OrderQuery> sorts) {
        if (!CollectionUtils.isEmpty(sorts)) {
            return sorts.stream().map(s -> s.toSort()).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * @param clazz
     * @return java.util.List<co.elastic.clients.elasticsearch._types.query_dsl.FieldAndFormat>
     * @author luyc
     * @Description 通过类查询有哪些字段需要查询
     * @Date 2022/12/30 16:12
     **/
    private List<String> getFields(Class clazz) {
        List<String> list = new ArrayList<>();
        List<java.lang.reflect.Field> fieldList = new ArrayList<>();
        /**
         * 将父类的属性加入
         */
        Collections.addAll(fieldList, clazz.getDeclaredFields());
        Class superClass = clazz.getSuperclass();
        while (superClass != null) {
            java.lang.reflect.Field[] superFields = superClass.getDeclaredFields();
            Collections.addAll(fieldList, superFields);
            superClass = superClass.getSuperclass();
        }

        for (java.lang.reflect.Field field : fieldList) {
            JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
            if (jsonProperty != null) {
                list.add(jsonProperty.value());
            }
        }
        return list;
    }


    /**
     * @return co.elastic.clients.elasticsearch.core.SearchRequest
     * @Author luyc
     * @Description 创建文档查询请求
     * @Date 2022/10/14 10:18
     * @Param [index, query, sorts, size, queryAll]
     **/
    private SearchRequest search(String index, Query query, List<OrderQuery> sorts, Integer size, boolean queryAll, Class clazz) {
        SearchRequest searchRequest = SearchRequest.of(builder -> {
            builder.index(index);
            if (clazz != null && !clazz.equals(Void.class)) {
                builder.source(SourceConfig.of(s -> s.filter(source -> source.includes(getFields(clazz)))));
            }
            builder.query(query);
            if (queryAll) {
                //track_total_hits": true
                builder.size(MAX_SIZE);
            }
            if (!CollectionUtils.isEmpty(sorts)) {
                builder.sort(sort(sorts));
            }
            if (size != null && size.intValue() >= 0) {
                builder.size(size);
            }
            return builder;
        });
        return searchRequest;
    }

    /**
     * @return co.elastic.clients.elasticsearch.core.SearchRequest
     * @Author luyc
     * @Description 创建文档查询请求
     * @Date 2022/10/14 10:18
     * @Param [index, query, sorts, size, queryAll]
     **/
    private SearchRequest search(String index, Query query, List<OrderQuery> sorts, List<AggQuery> aggregations) {
        SearchRequest searchRequest = SearchRequest.of(builder -> {
            builder.index(index);
            builder.query(query);


            if (!CollectionUtils.isEmpty(sorts)) {
                builder.sort(sort(sorts));
            }

            if (!CollectionUtils.isEmpty(aggregations)) {
                Map<String, Aggregation> aggregationMap = new HashMap<>();
                for (AggQuery aggregation : aggregations) {
                    aggregationMap.put(aggregation.getAggName(), aggregation.getAggregation());
                }
                builder.aggregations(aggregationMap);
            }
            return builder;
        });
        return searchRequest;
    }

    /**
     * @return co.elastic.clients.elasticsearch.core.SearchRequest
     * @Author luyc
     * @Description 创建文档查询请求
     * @Date 2022/10/14 10:18
     * @Param [index, query, sorts, size, queryAll]
     **/
    private SearchRequest search(String index, Query query, AggQuery... aggregations) {
        SearchRequest searchRequest = SearchRequest.of(builder -> {
            builder.index(index);
            builder.query(query);
            builder.trackTotalHits(t -> t.enabled(true));
            builder.size(0);
            if (aggregations != null && aggregations.length > 0) {
                Map<String, Aggregation> aggregationMap = new HashMap<>();
                for (AggQuery aggregation : aggregations) {
                    aggregationMap.put(aggregation.getAggName(), aggregation.getAggregation());
                }
                builder.aggregations(aggregationMap);
            }
            return builder;
        });
        return searchRequest;
    }

    /**
     * @return co.elastic.clients.elasticsearch.core.SearchRequest
     * @Author luyc
     * @Description 创建文档查询请求
     * @Date 2022/10/14 10:18
     * @Param [index, query, sorts, size, queryAll]
     **/
    private SearchRequest search(String index, Query query, List<OrderQuery> sorts, Class clazz,PageQuery pageQuery) {
        SearchRequest searchRequest = SearchRequest.of(builder -> {
            builder.index(index);
            if (clazz != null && !clazz.equals(Void.class)) {
                builder.source(SourceConfig.of(s -> s.filter(source -> source.includes(getFields(clazz)))));
            }
            builder.query(query);
            if (!CollectionUtils.isEmpty(sorts)) {
                builder.sort(sort(sorts));
            }


            if(pageQuery != null){
                builder.from(pageQuery.getFrom());
                builder.size(pageQuery.getPageSize());
            }
            return builder;
        });
        return searchRequest;
    }

    /**
     * @return co.elastic.clients.elasticsearch.core.SearchRequest
     * @Author luyc
     * @Description 创建文档查询请求
     * @Date 2022/10/14 10:18
     * @Param [index, query, sorts, size, queryAll]
     **/
    private SearchRequest search(String index, Query query, List<OrderQuery> sorts, AggQuery aggregation) {
        SearchRequest searchRequest = SearchRequest.of(builder -> {
            builder.index(index);
            builder.query(query);
            builder.trackTotalHits(t -> t.enabled(true));
            builder.size(0);
            if (aggregation != null) {
                builder.aggregations(aggregation.getAggName(), aggregation.getAggregation());
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
            if (StringUtils.hasLength(index)) {
                builder.index(index);
            }
            return builder;
        });
        List<IndicesRecord> records = client.cat().indices(request).valueBody();
        return records;
    }

    /**
     * @return co.elastic.clients.elasticsearch._types.mapping.TypeMapping
     * @Author luyc
     * @Description 查询索引的字段结构
     * @Date 2022/10/13 15:33
     * @Param [index]
     **/
    public List<Field> selFields(String index) throws IOException {
        GetIndexResponse response = client.indices().get(GetIndexRequest.of(builder -> {
            List<String> list = new ArrayList<>();
            builder.index(index);
            return builder;
        }));
        IndexState result = response.get(index);
        TypeMapping mapping = result.mappings();
        Map<String, Property> fields = mapping.properties();
        Iterator<Map.Entry<String, Property>> iterator = fields.entrySet().iterator();
        List<Field> fieldList = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry<String, Property> entry = iterator.next();
            String fieldName = entry.getKey();
            Property property = entry.getValue();
            fieldList.add(new Field(fieldName, property));
        }
        return fieldList;
    }

    /**
     * @author luyc
     * @Description 根据id查询文档
     * @Date 2023/1/3 15:33
     * @param index
     * @param id
     * @param documentType
     * @return T
     **/
    public <T> T selById(String index,String id,Class<T> documentType) throws IOException {
        GetResponse<T> response = client.get(g -> g
                        .index(index)
                        .id(id),
                documentType
        );
        if(response.found()){
            return response.source();
        }else{
            return null;
        }
    }

    /**
     * @return T
     * @Author luyc
     * @Description 查询单个文档
     * @Date 2022/10/13 16:10
     * @Param [index, query, documentType]
     **/
    public <T> T selSingleDocument(String index, Query query, Class<T> documentType) throws IOException {
        SearchRequest searchRequest = search(index, query, null, 1, false, documentType);
        SearchResponse<T> response = client.search(searchRequest, documentType);
        List<Hit<T>> list = response.hits().hits();
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        T result = list.get(0).source();
        if (BaseEntity.class.isAssignableFrom(documentType)) {
            BaseEntity baseEntity = (BaseEntity) result;
            baseEntity.setId(list.get(0).id());
        }
        return result;
    }


    /**
     * @return java.util.List<T>
     * @Author luyc
     * @Description 查询列表
     * @Date 2022/10/14 10:21
     * @Param [index, query, sorts, documentType]
     **/
    public <T> List<T> selList10000(String index, Query query, List<OrderQuery> sorts, Class<T> documentType) throws IOException {
        SearchRequest searchRequest = search(index, query, sorts, null, true, documentType);
        SearchResponse<T> response = client.search(searchRequest, documentType);
        List<Hit<T>> hits = response.hits().hits();
        List<T> list = new ArrayList<>(hits.size());
        Iterator<Hit<T>> iterator = hits.iterator();
        while (iterator.hasNext()) {
            Hit<T> hit = iterator.next();
            T t = hit.source();
            if (BaseEntity.class.isAssignableFrom(documentType)) {
                BaseEntity baseEntity = (BaseEntity) t;
                baseEntity.setId(hit.id());
            }
            list.add(t);
        }
        return list;
    }

    /**
     * @author luyc
     * @Description 查询数量
     * @Date 2023/4/20 14:20
     * @param index
     * @param query
     * @return java.lang.Long
     **/
    public Long count(String index,Query query) throws IOException {
        CountRequest request = CountRequest.of(b->{
            b.index(index)
                    .query(query);
            return b;
        });
        CountResponse response = client.count(request);
        return Long.valueOf(response.count());
    }


    /**
     * @author luyc
     * @Description 10000条数据以内分页查询
     * @Date 2023/4/20 14:36
     * @param index
     * @param query
     * @param sorts
     * @param documentType
     * @param pageQuery
     * @return com.xczk.pacs_pool.esclient.common.PageVO<T>
     **/
    public <T> PageVO<T> selPageIn10000(String index, Query query, List<OrderQuery> sorts, Class<T> documentType,PageQuery pageQuery) throws IOException {

        Long count = count(index,query);

        SearchRequest searchRequest = search(index, query, sorts, documentType,pageQuery);
        SearchResponse<T> response = client.search(searchRequest, documentType);
        List<Hit<T>> hits = response.hits().hits();
        List<T> list = new ArrayList<>(hits.size());
        Iterator<Hit<T>> iterator = hits.iterator();
        while (iterator.hasNext()) {
            Hit<T> hit = iterator.next();
            T t = hit.source();
            if (BaseEntity.class.isAssignableFrom(documentType)) {
                BaseEntity baseEntity = (BaseEntity) t;
                baseEntity.setId(hit.id());
            }
            list.add(t);
        }
        PageVO<T> pageVO = new PageVO<>();
        pageVO.setRecordList(list);
        pageVO.setTotal(count.longValue());
        pageVO.setPageNumber(pageQuery.getPageNumber());
        pageVO.setTotalNumber(PageVO.calculateTotalNumber(count,pageQuery.getPageSize()));
        return pageVO;
    }

    /**
     * @param index
     * @param document
     * @return boolean
     * @author luyc
     * @Description 插入一个文档
     * @Date 2023/1/3 14:51
     **/
    public <T> boolean save(String index, T document) throws IOException {
        if(BaseEntity.class.isAssignableFrom(document.getClass())){
            ((BaseEntity) document).setCreateTime(LocalDateTime.now());
            ((BaseEntity) document).setUpdateTime(LocalDateTime.now());
        }
        IndexRequest<T> indexRequest = IndexRequest.of(s -> s.index(index).document(document).refresh(Refresh.True));
        IndexResponse response = client.index(indexRequest);
        return Result.Created.equals(response.result());
    }

    /**
     * @author luyc
     * @Description 分根据查询删除
     * @Date 2023/4/11 14:37
     * @param index
     * @param query
     * @return boolean
     **/
    public boolean deleteByQuery(String index,Query query) throws IOException {
        DeleteByQueryRequest queryRequest = DeleteByQueryRequest.of(d->d.index(index).query(query));
        DeleteByQueryResponse response = client.deleteByQuery(queryRequest);
        log.info("delete {} rows from index {}",response.deleted(),index);
        return response.failures().size() == 0;
    }


    /**
     * @param index
     * @param document
     * @return com.luyc.esclient.common.OperateResult
     * @author luyc
     * @Description 插入一个文档
     * @Date 2023/1/3 14:52
     **/
    public <T extends BaseEntity> OperateResult saveDocument(String index, T document) throws IOException {
        document.setCreateTime(LocalDateTime.now());
        document.setUpdateTime(LocalDateTime.now());
        IndexRequest<T> indexRequest = IndexRequest.of(s -> s.index(index).document(document).refresh(Refresh.True));
        IndexResponse response = client.index(indexRequest);
        return new OperateResult(response.id(), Result.Created.equals(response.result()));
    }

    /**
     * @param index
     * @param document
     * @return boolean
     * @author luyc
     * @Description 插入一个文档
     * @Date 2023/1/3 14:51
     **/
    public <T> boolean save(String index, String id, T document) throws IOException {
        if(BaseEntity.class.isAssignableFrom(document.getClass())){
            ((BaseEntity) document).setCreateTime(LocalDateTime.now());
            ((BaseEntity) document).setUpdateTime(LocalDateTime.now());
        }
        IndexRequest<T> indexRequest = IndexRequest.of(s -> s.index(index).id(id).document(document).refresh(Refresh.True));
        IndexResponse response = client.index(indexRequest);
        return Result.Created.equals(response.result());
    }

    /**
     * @param index
     * @param document
     * @return com.luyc.esclient.common.OperateResult
     * @author luyc
     * @Description 插入一个文档
     * @Date 2023/1/3 14:52
     **/
    public <T extends BaseEntity> OperateResult saveDocument(String index, String id, T document) throws IOException {
        document.setCreateTime(LocalDateTime.now());
        document.setUpdateTime(LocalDateTime.now());
        IndexRequest<T> indexRequest = IndexRequest.of(s -> s.index(index).id(id).document(document).refresh(Refresh.True));
        IndexResponse response = client.index(indexRequest);
        return new OperateResult(response.id(), Result.Created.equals(response.result()));
    }


    /**
     * @author luyc
     * @Description 批量插入
     * @Date 2023/1/3 15:29
     * @param index
     * @param list
     * @return com.luyc.esclient.common.Operate
     **/
    public <T extends BaseEntity> Operate saveByBulk(String index, List<T> list) throws IOException {
        List<BulkOperation> operations = new ArrayList<>();
        for (T document : list) {
            document.setCreateTime(LocalDateTime.now());
            document.setUpdateTime(LocalDateTime.now());
            operations.add(
                    BulkOperation.of(b -> b.index(
                                    s -> {
                                        s.index(index)
                                                .document(document);
                                        if (document.getId() != null) {
                                            s.id(document.getId());
                                        }
                                        return s;
                                    }
                            )
                    )

            );
        }
        BulkRequest bulkRequest = BulkRequest.of(b -> b.refresh(Refresh.True).operations(operations));
        BulkResponse response = client.bulk(bulkRequest);
        if(response.errors()) {
            List<OperateResult> l = new ArrayList<>();
            int i = 1;
            int s = 0;
            for (BulkResponseItem r : response.items()) {
                if (r.error() != null){
                    l.add(new OperateResult(false,Integer.valueOf(i),r.error().reason()));
                }else{
                    s++;
                }
                i++;
            }
            log.warn("saving data into index {} has failed number for {},total:{}",index,l.size(),list.size());
            return Operate.failed(s,l);
        }else{
            log.info("saving data into index {},total:{}",index,list.size());
            return Operate.success(Integer.valueOf(list.size()));
        }
    }


    /**
     * @param index
     * @param query
     * @param aggQuery
     * @return physical.common.pojo.AggResult
     * @author luyc
     * @Description 聚合查询
     * @Date 2022/12/7 12:18
     **/
    public AggResult selByAgg(String index, Query query, AggQuery aggQuery) throws IOException {
        SearchRequest searchRequest = search(index, query, aggQuery);

        SearchResponse searchResponse = client.search(searchRequest, Void.class);
        Map<String, Aggregate> agg = searchResponse.aggregations();
        AggResult aggResult = new AggResult(aggQuery.getAggName());
        agg(agg.get(aggQuery.getAggName()), aggResult);
        return aggResult;
    }

    /**
     * @param index
     * @param query
     * @param aggs
     * @return java.util.List<physical.common.pojo.AggResult>
     * @author luyc
     * @Description 多聚合查询
     * @Date 2022/12/22 10:45
     **/
    public List<AggResult> selByAggs(String index, Query query, AggQuery... aggs) throws IOException {
        SearchRequest searchRequest = search(index, query, aggs);
        SearchResponse searchResponse = client.search(searchRequest, Void.class);
        Map<String, Aggregate> aggsMap = searchResponse.aggregations();
        List<AggResult> list = new ArrayList<>();
        for (AggQuery tempAgg : aggs) {
            Aggregate agg = aggsMap.get(tempAgg.getAggName());
            AggResult aggResult = new AggResult(tempAgg.getAggName());
            agg(agg, aggResult);
            list.add(aggResult);
        }
        return list;
    }

    /**
     * @param agg    聚合
     * @param result agg这层聚合的值
     * @return physical.common.Record
     * @author luyc
     * @Description 使用递归的方式处理多层聚合
     * @Date 2022/12/1 11:34
     **/
    private AggResult agg(Aggregate agg, AggResult result) {
        if (agg.isSterms()) {
            result.initBucket();
            for (StringTermsBucket bucket : agg.sterms().buckets().array()) {
                Bucket b = new Bucket(bucket.key(), bucket.docCount());
                result.addBucket(b);
                b.initSubAggs();
                Map<String, Aggregate> aggregations = bucket.aggregations();
                for (Map.Entry<String, Aggregate> tmp : aggregations.entrySet()) {
                    AggResult r = new AggResult(tmp.getKey());
                    agg(tmp.getValue(), r);
                    b.addSubAgg(r);
                }
            }
        } else if (agg.isLterms()) {
            result.initBucket();
            for (LongTermsBucket bucket : agg.lterms().buckets().array()) {
                Bucket b = new Bucket(bucket.key(), bucket.docCount());
                result.addBucket(b);
                b.initSubAggs();
                Map<String, Aggregate> aggregations = bucket.aggregations();
                for (Map.Entry<String, Aggregate> tmp : aggregations.entrySet()) {
                    AggResult r = new AggResult(tmp.getKey());
                    agg(tmp.getValue(), r);
                    b.addSubAgg(r);
                }
            }
        } else if (agg.isDterms()) {
            result.initBucket();
            for (DoubleTermsBucket bucket : agg.dterms().buckets().array()) {
                Bucket b = new Bucket(bucket.keyAsString(), bucket.docCount());
                result.addBucket(b);
                b.initSubAggs();
                Map<String, Aggregate> aggregations = bucket.aggregations();
                for (Map.Entry<String, Aggregate> tmp : aggregations.entrySet()) {
                    AggResult r = new AggResult(tmp.getKey());
                    agg(tmp.getValue(), r);
                    b.addSubAgg(r);
                }
            }
        } else if (agg.isDateHistogram()) {
            DateHistogramAggregate dateHistogramAggregate = agg.dateHistogram();
            result.initBucket();
            for (DateHistogramBucket dateBucket : dateHistogramAggregate.buckets().array()) {
                Bucket b = new Bucket(dateBucket.keyAsString(), dateBucket.docCount());
                result.addBucket(b);
                b.initSubAggs();
                Map<String, Aggregate> aggregations = dateBucket.aggregations();
                for (Map.Entry<String, Aggregate> tmp : aggregations.entrySet()) {
                    AggResult r = new AggResult(tmp.getKey());
                    agg(tmp.getValue(), r);
                    b.addSubAgg(r);
                }
            }
        } else {
            if (agg.isSum()) {
                result.setValue(String.valueOf(agg.sum().value()));
                result.setDoubleValue(agg.sum().value());
            } else if (agg.isValueCount()) {
                result.setValue(agg.valueCount().valueAsString());
                result.setDoubleValue(agg.valueCount().value());
            } else if (agg.isCardinality()) {
                result.setValue(String.valueOf(agg.cardinality().value()));
                result.setLongValue(agg.cardinality().value());
            } else if (agg.isAvg()) {
                result.setValue(agg.avg().valueAsString());
                result.setDoubleValue(agg.avg().value());
            }
        }
        return result;
    }

}
