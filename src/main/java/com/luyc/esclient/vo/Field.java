package com.luyc.esclient.vo;

import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.json.JsonData;

/**
 * index索引名称
 * @author luyc
 * @since 2022/10/13 15:34
 */
public class Field {
    private String name;
    private Property value;

    public Field(String name, Property value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Property getValue() {
        return value;
    }

    public void setValue(Property value) {
        this.value = value;
    }
}
