package com.luyc.esclient.util;

import co.elastic.clients.elasticsearch.cat.indices.IndicesRecord;
import com.luyc.esclient.EsClientApplication;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

/**
 * @author luyc
 * @since 2022/10/11 17:26
 */
public class DbKitTest extends EsClientApplication {
    @Autowired
    DbKit dbKit;
    public void testIndexList() throws IOException {
        List<IndicesRecord> list = dbKit.selIndices();

    }
}
