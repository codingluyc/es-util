package com.luyc.esclient.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author luyc
 * @since 2022/12/7 11:27
 */
public class Bucket {
    private String key;

    private Long docCount;

    private List<AggResult> subAggs;

    public Bucket(String key, Long docCount) {
        this.key = key;
        this.docCount = docCount;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getDocCount() {
        return docCount;
    }

    public void setDocCount(Long docCount) {
        this.docCount = docCount;
    }

    public List<AggResult> getSubAggs() {
        return subAggs;
    }

    public void setSubAggs(List<AggResult> subAggs) {
        this.subAggs = subAggs;
    }

    public void initSubAggs(){
        this.subAggs = new ArrayList<>();
    }

    public void addSubAgg(AggResult sub){
        this.subAggs.add(sub);
    }

    public Map<String,AggResult> getAggs(){
        if(this.subAggs == null){
            return null;
        }
        return subAggs.stream().collect(Collectors.toMap(s->s.getAggName(),s->s));
    }
}
