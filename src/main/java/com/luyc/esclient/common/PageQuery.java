package com.luyc.esclient.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <description>
 *
 * @author luyc
 * @since 2022/8/24 17:03
 */
@ApiModel(value = "分页查询条件")
public class PageQuery {

    @ApiModelProperty(value = "页码")
    private Integer pageNumber;

    @ApiModelProperty(value = "数量")
    private Integer pageSize;

    public PageQuery() {
    }

    public PageQuery(Integer pageNumber, Integer pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public Integer getPageNumber() {
        if(pageNumber == null){
            pageNumber = 1;
        }
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {

        if(pageSize == null){
            pageSize = 0;
        }
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
