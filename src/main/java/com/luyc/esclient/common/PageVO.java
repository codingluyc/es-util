package com.luyc.esclient.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <description>
 * 分页结果
 * @author luyc
 * @since 2022/8/26 14:34
 */
@ApiModel
public class PageVO<T> {
    @ApiModelProperty(value = "数据总量")
    private long total;

    @ApiModelProperty(value = "数据")
    private List<T> recordList;

    @ApiModelProperty(value = "页码")
    private int pageNumber;

    @ApiModelProperty(value = "总页数")
    private int totalNumber;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<T> recordList) {
        this.recordList = recordList;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }
}
