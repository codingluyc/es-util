package com.luyc.esclient.common;

import co.elastic.clients.json.JsonData;
import com.luyc.esclient.util.StrKit;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 用于定义索引所含字段
 * @author luyc
 * @since 2022/9/13 15:51
 */
public class IndexField {
    //字段名称（不可为空）
    private String name;

    //字段类型（不可为空）
    private String type;

    //格式（可为空）
    private String format;

    //别名
    private String path;

    //子字段
    private List<IndexField> fields;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<IndexField> getFields() {
        return fields;
    }

    public void setFields(List<IndexField> fields) {
        this.fields = fields;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @Author luyc
     * @Description 将对象转换成map，用于创建索引
     * @Date 16:01 2022/9/13
     * @Param []
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    public JsonData toJsonData(){
        Map<String,Object> map = new HashMap<>();
        map.put("type",this.type);
        if(StrKit.notBlank(this.format)){
            map.put("format",this.format);
        }
        if(!CollectionUtils.isEmpty(fields)){
            map.put("fields",subFieldMap());
        }
        if (StrKit.notBlank(this.path)){
            map.put("path",this.path);
        }
        return JsonData.of(map);

    }


    /**
     * @Author luyc
     * @Description 将对象转换成map，用于创建索引
     * @Date 16:01 2022/9/13
     * @Param []
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    public Map<String,Object> toMap(){
        Map<String,Object> map = new HashMap<>();
        map.put("type",this.type);
        if(StrKit.notBlank(this.format)){
            map.put("format",this.format);
        }
        if(!CollectionUtils.isEmpty(fields)){
            map.put("fields",subFieldMap());
        }
        if (StrKit.notBlank(this.path)){
            map.put("path",this.path);
        }
        return map;

    }

    /**
     * @Author luyc
     * @Description 将子字段转换成map，用于创建索引
     * @Date 16:02 2022/9/13
     * @Param []
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    private Map<String,Object> subFieldMap(){
        Map<String,Object> map = new HashMap<>();

        if(!CollectionUtils.isEmpty(fields)){
            Iterator<IndexField> iterator = fields.listIterator();
            while(iterator.hasNext()){
                IndexField field = iterator.next();
                map.put(field.getName(),field.toMap());
            }
        }
        return map;
    }
}
