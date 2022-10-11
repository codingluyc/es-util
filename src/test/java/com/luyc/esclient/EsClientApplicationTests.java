package com.luyc.esclient;

import com.luyc.esclient.util.DbKit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EsClientApplicationTests {

    @Autowired
    public DbKit dbKit;

    @Test
    void contextLoads() {
    }

}
