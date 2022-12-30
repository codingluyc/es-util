package com.luyc.esclient.common;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;

/**
 * @author luyc
 * @since 2022/12/22 10:58
 */
public class AggQuery {
    private Aggregation aggregation;
    private String aggName;

    public AggQuery() {
    }

    public AggQuery(String aggName, Aggregation aggregation) {
        this.aggregation = aggregation;
        this.aggName = aggName;
    }

    public Aggregation getAggregation() {
        return aggregation;
    }

    public void setAggregation(Aggregation aggregation) {
        this.aggregation = aggregation;
    }

    public String getAggName() {
        return aggName;
    }

    public void setAggName(String aggName) {
        this.aggName = aggName;
    }
}
