package com.qunar.hotconfig.service;

import com.qunar.hotconfig.model.UserPermissionModel;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/app.xml" })
@TransactionConfiguration
public class UserPermissionServiceTest {

    private static final Logger LOG = LoggerFactory.getLogger(UserPermissionServiceTest.class);

    @Autowired
    private UserPermissionService userPermissionService;

    @Test
    public void testSelectByFileId() throws Exception {
        List<UserPermissionModel> list = userPermissionService.selectByFileId("flight_info");
        LOG.info("根据表名查询总大小：{}", list.size());
        for (UserPermissionModel userPermissionModel : list) {
            LOG.info("根据表名查询的每条信息：{}", userPermissionModel);
        }
    }

    @Test
    public void testSelectByUserIdAndFileId() throws Exception {
        LOG.info("根据用户名和表明查询：{}", userPermissionService.selectByUserIdAndFileId("shuai.lv", "flight_info"));
    }

    @Test
    @Rollback
    public void testInsert() throws Exception {
        UserPermissionModel userPermissionModel = new UserPermissionModel();
        userPermissionModel.setUserId("llvvss");
        userPermissionModel.setFileId("fuel_tax");
        userPermissionModel.setModifyPermission(false);
        userPermissionModel.setPublishPermission(true);
        userPermissionService.insert(userPermissionModel);
        LOG.info("新插入的键值：{}", userPermissionModel.getId());
    }

    @Test
    @Rollback
    public void testDeleteById() throws Exception {
        LOG.info("删除id=100：{}", userPermissionService.deleteById(100));
    }

    @Test
    @Rollback
    public void testUpdateById() throws Exception {
        UserPermissionModel userPermissionModel = new UserPermissionModel();
        userPermissionModel.setId(7);
        userPermissionModel.setUserId("shuai.lv");
        userPermissionModel.setFileId("flight_info");
        userPermissionModel.setModifyPermission(false);
        userPermissionModel.setPublishPermission(true);
        userPermissionService.updateById(userPermissionModel);
    }

    @Test
    public void testDoubleCheck() throws Exception {
        UserPermissionModel userPermissionModel = new UserPermissionModel();
        userPermissionModel.setUserId("shuai.lv");
        userPermissionModel.setFileId("flight_info");
        userPermissionModel.setModifyPermission(true);
        userPermissionModel.setPublishPermission(false);
        UserPermissionModel userPermissionModel2 = new UserPermissionModel();
        userPermissionModel2.setUserId("caiyun.lu");
        userPermissionModel2.setFileId("flight_info");
        userPermissionModel2.setModifyPermission(false);
        userPermissionModel2.setPublishPermission(false);
        UserPermissionModel userPermissionModel3 = new UserPermissionModel();
        userPermissionModel3.setUserId("caiyun.lu");
        userPermissionModel3.setFileId("flight_info");
        userPermissionModel3.setModifyPermission(true);
        userPermissionModel3.setPublishPermission(true);
        LOG.info("Double Check: {}", userPermissionService.doubleCheck(userPermissionModel));
        LOG.info("Double Check: {}", userPermissionService.doubleCheck(userPermissionModel2));
        LOG.info("Double Check: {}", userPermissionService.doubleCheck(userPermissionModel3));
    }
}