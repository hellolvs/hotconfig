package com.qunar.hotconfig.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/app.xml" })
public class ConfFileListServiceTest {

    private static final Logger LOG = LoggerFactory.getLogger(ConfFileListServiceTest.class);

    @Autowired
    private ConfFileListService confFileListService;

    @Test
    public void testSelectAllFileId() throws Exception {
        LOG.info("全部配置文件信息：{}", confFileListService.selectAllFileId());
    }
}