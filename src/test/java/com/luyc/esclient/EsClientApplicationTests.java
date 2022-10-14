package com.luyc.esclient;

import com.luyc.esclient.util.DbKit;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EsClientApplicationTests {

    public static final Logger log = LoggerFactory.getLogger(EsClientApplicationTests.class);
    @Autowired
    public DbKit dbKit;

    @Test
    void contextLoads() {
    }

}
