package com.qunar.hotconfig.service.impl;

import com.qunar.hotconfig.service.ConfFileService;
import com.qunar.hotconfig.service.PublishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author shuai.lv
 * @date 2017/4/8.
 */
@Service("PublishServiceImpl")
public class PublishServiceImpl implements PublishService {

    private static final String ONLINEDBNAME = "conffilesdb"; // 对外提供数据的数据库
    private static final String MANAGEDBNAME = "conffilesmanage"; // 热发布修改及备份的数据库

    @Autowired
    private ConfFileService confFileService;

    @Override
    @Transactional
    public long publishConfFile(String userId, String fileId) {
        List<Map> list = confFileService.selectByTableName(MANAGEDBNAME + "." + fileId);
        confFileService.deleteTable(ONLINEDBNAME + "." + fileId);
        confFileService.batchInsertByTableName(ONLINEDBNAME + "." + fileId, list);
        long publishTime = System.currentTimeMillis();
        for (Map map : list) {
            map.remove("id");
            map.put("publishtime", publishTime);
            map.put("publishuser", userId);
        }
        confFileService.batchInsertByTableName(MANAGEDBNAME + "." + fileId + "bak", list);
        return publishTime;
    }
}
