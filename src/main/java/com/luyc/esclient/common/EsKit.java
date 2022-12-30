package com.luyc.esclient.common;

import co.elastic.clients.elasticsearch._types.FieldValue;

import java.util.ArrayList;
import java.util.List;

/**
 * es工具类
 * @author luyc
 * @since 2022/12/30 10:53
 */
public class EsKit {

    /**
     * @author luyc
     * @Description terms查询需要的long类型的FieldValue列表
     * @Date 2022/12/30 15:56
     * @param values
     * @return java.util.List<co.elastic.clients.elasticsearch._types.FieldValue>
     **/
    public static List<FieldValue> longValues(List<Long> values){
        List<FieldValue> list = new ArrayList<>();
        for(Long l:values){
            list.add(FieldValue.of(l));
        }
        return list;
    }

    /**
     * @author luyc
     * @Description terms查询需要的long类型的FieldValue列表
     * @Date 2022/12/30 15:56
     * @param values
     * @return java.util.List<co.elastic.clients.elasticsearch._types.FieldValue>
     **/
    public static List<FieldValue> longValues(Long... values){
        List<FieldValue> list = new ArrayList<>();
        for(Long l:values){
            list.add(FieldValue.of(l));
        }
        return list;
    }


    /**
     * @author luyc
     * @Description terms查询需要的string类型的FieldValue列表
     * @Date 2022/12/30 15:56
     * @param values
     * @return java.util.List<co.elastic.clients.elasticsearch._types.FieldValue>
     **/
    public static List<FieldValue> strValues(List<String> values){
        List<FieldValue> list = new ArrayList<>();
        for(String l:values){
            list.add(FieldValue.of(l));
        }
        return list;
    }

    /**
     * @author luyc
     * @Description terms查询需要的string类型的FieldValue列表
     * @Date 2022/12/30 15:56
     * @param values
     * @return java.util.List<co.elastic.clients.elasticsearch._types.FieldValue>
     **/
    public static List<FieldValue> strValues(String... values){
        List<FieldValue> list = new ArrayList<>();
        for(String l:values){
            list.add(FieldValue.of(l));
        }
        return list;
    }

}
