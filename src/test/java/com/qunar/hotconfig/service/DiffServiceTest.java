package com.qunar.hotconfig.service;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * Created by shadandan on 17/4/7.
 */
public class DiffServiceTest {

    private static final Logger LOG = LoggerFactory.getLogger(DiffServiceTest.class);
    private DiffService diffService;

    @Before
    public void init() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring/app.xml");
        diffService = applicationContext.getBean(DiffService.class);
    }

    @Test
    public void test() {
        List list = diffService.configDiff("six_little_camel");
        LOG.info("list:{}", list);
    }
}
