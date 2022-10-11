package com.luyc.esclient.util;

import co.elastic.clients.elasticsearch._types.mapping.AllField;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch._types.mapping.PropertyVariant;
import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch.cat.indices.IndicesRecord;
import com.luyc.esclient.EsClientApplicationTests;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author luyc
 * @since 2022/10/11 17:26
 */
public class DbKitTest extends EsClientApplicationTests {


    /**
     * 测试查询索引列表
     * @throws IOException
     */
    @Test
    public void testIndexList() throws IOException {
//        List<IndicesRecord> list = dbKit.selIndices();
        TypeMapping mapping = dbKit.selFields("test_index");
        Map<String, Property> fields =  mapping.properties();
        Property variant = fields.get("birthday");
        PropertyVariant values = (PropertyVariant) variant._get();

    }
}
