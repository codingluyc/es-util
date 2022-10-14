package com.luyc.esclient.common;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <description>
 * es排序字段
 * @author luyc
 * @since 2022/8/12 18:05
 */
@ApiModel(value = "排序查询")
public class OrderQuery {
    @ApiModelProperty(value = "排序字段")
    private String sortField;

    @ApiModelProperty(value = "顺序还是逆序 asc || desc")
    private String order;

    public OrderQuery() {
    }

    public OrderQuery(String sortField) {
        this.sortField = sortField;
        this.order = "desc";
    }

    public OrderQuery(String sortField, String order) {
        this.sortField = sortField;
        this.order = order;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        if(!order.equals("desc") && !order.equals("asc")){
            throw new IllegalArgumentException("排序方式仅有desc和asc");
        }
        this.order = order;
    }

    public OrderQuery desc(){
        this.order = "desc";
        return this;
    }

    public OrderQuery asc(){
        this.order = "asc";
        return this;
    }


    public SortOptions toSort(){
        SortOptions sortOptions = SortOptions.of(builder -> builder.field(f->f.field(this.sortField).order(this.sortOrder())));
        return sortOptions;
    }

    private SortOrder sortOrder(){
        if(this.order.equals("desc")){
            return SortOrder.Desc;
        }else{
            return SortOrder.Asc;
        }
    }

}
