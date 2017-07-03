package com.qunar.hotconfig.service;

import com.google.common.collect.Maps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.util.List;
import java.util.Map;

/**
 * Created by llnn.li on 2017/4/7.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/app.xml" })
@TransactionConfiguration
public class ConfFileServiceTest {

    private static final Logger LOG = LoggerFactory.getLogger(ConfFileServiceTest.class);

    @Autowired
    private ConfFileService confFileService;

    @Test
    @Rollback
    public void testDelete() {
        LOG.info("删除id=1：{}", confFileService.deleteById("flight_info", 1));
    }

    @Test
    public void testSelectByTableName(){
        List<Map> list = confFileService.selectByTableName("flight_info");
        LOG.info("根据表名查询总大小：{}", list.size());
    }

    @Test
    @Rollback
    public void testInsertByTableName() {
        Map<String, Object> clunmsValuesMap = Maps.newHashMap();
        clunmsValuesMap.put("dep_code", "CSD");
        clunmsValuesMap.put("arr_code", "SED");
        clunmsValuesMap.put("tax", 150);
        confFileService.insertByTableName("fuel_tax", clunmsValuesMap);
        LOG.info("插入完成");
    }

    @Test
    @Rollback
    public void testUpdateByPrimaryKey() {
        Map<String, Object> clunmsValuesMap = Maps.newHashMap();
        clunmsValuesMap.put("id", 1);
        clunmsValuesMap.put("dep_code", "CSD");
        clunmsValuesMap.put("arr_code", "SED");
        clunmsValuesMap.put("tax", 150);
        confFileService.updateByPrimaryKey("fuel_tax", clunmsValuesMap);
    }
}
