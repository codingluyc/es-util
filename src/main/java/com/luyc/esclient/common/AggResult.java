package com.luyc.esclient.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * es 聚合参数
 * @author luyc
 * @since 2022/12/7 11:15
 */
public class AggResult {

    private String aggName;

    private String value;

    private List<Bucket> buckets;
    private List<AggResult> subAggs;

    public AggResult() {
    }

    public AggResult(String aggName) {
        this.aggName = aggName;
    }

    public AggResult(String aggName, String value) {
        this.aggName = aggName;
        this.value = value;
    }

    public String getAggName() {
        return aggName;
    }

    public void setAggName(String aggName) {
        this.aggName = aggName;
    }

    public String getValue() {
        return value;
    }

    public Double getDoubleValue(){

        return new Double(new BigDecimal(Double.valueOf(this.value)).setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue());
    }

    public Integer getIntegerValue(){
        return new Integer(new Double(this.value).intValue());
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<AggResult> getSubAggs() {
        return subAggs;
    }

    public void setSubAggs(List<AggResult> subAggs) {
        this.subAggs = subAggs;
    }

    public List<Bucket> getBuckets() {
        return buckets;
    }

    public void setBuckets(List<Bucket> buckets) {
        this.buckets = buckets;
    }

    public void initBucket(){
        this.buckets = new ArrayList<>();
    }

    public void addBucket(Bucket bucket){
        this.buckets.add(bucket);
    }

    public void initSubAggs(){
        this.subAggs = new ArrayList<>();
    }


}
