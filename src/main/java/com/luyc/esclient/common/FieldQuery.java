package com.luyc.esclient.common;

import com.luyc.esclient.util.DbKit;
import com.luyc.esclient.util.QueryBuilder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author luyc
 * @since 2022/10/10 17:24
 */
@ApiModel
public class FieldQuery {
    @ApiModelProperty(value = "字段")
    private String field;
    @ApiModelProperty(value = "值")
    private Object value;
    @ApiModelProperty(value = "查询类型")
    private QueryBuilder.BoolType type;

    public FieldQuery(String field, Object value, QueryBuilder.BoolType type) {
        this.field = field;
        this.value = value;
        this.type = type;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public QueryBuilder.BoolType getType() {
        return type;
    }

    public void setType(QueryBuilder.BoolType type) {
        this.type = type;
    }
}
