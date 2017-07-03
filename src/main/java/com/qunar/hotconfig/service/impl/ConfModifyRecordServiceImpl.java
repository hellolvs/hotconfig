package com.qunar.hotconfig.service.impl;

import com.qunar.hotconfig.dao.ConfModifyRecordDao;
import com.qunar.hotconfig.model.ConfModifyRecordModel;
import com.qunar.hotconfig.service.ConfFileService;
import com.qunar.hotconfig.service.ConfModifyRecordService;
import com.qunar.hotconfig.util.dateFormatUtil.MapDateFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by shadandan on 17/4/18.
 */
@Service
public class ConfModifyRecordServiceImpl implements ConfModifyRecordService {

    @Autowired
    private ConfModifyRecordDao confModifyRecordDao;
    @Autowired
    private ConfFileService confFileService;

    @Override
    public List<ConfModifyRecordModel> selectModifyRecordByFileId(String fileId) {
        return confModifyRecordDao.selectModifyRecordByFileId(fileId);
    }

    @Override
    public ConfModifyRecordModel selectModifyRecord(String fileId, Integer itemId) {
        return confModifyRecordDao.selectModifyRecord(fileId, itemId);
    }

    @Override
    public int insertConfModifyRecord(ConfModifyRecordModel confModifyRecordModel) {
        return confModifyRecordDao.insertConfModifyRecord(confModifyRecordModel);
    }

    @Override
    public int deleteModifyRecordByFileId(String fileId) {
        return confModifyRecordDao.deleteModifyRecordByFileId(fileId);
    }

    public int deleteModifyRecordById(Integer id) {
        return confModifyRecordDao.deleteModifyRecordById(id);
    }

    @Override
    public int updateModifyRecord(ConfModifyRecordModel confModifyRecordModel) {
        return confModifyRecordDao.updateModifyRecord(confModifyRecordModel);
    }

    /**
     * 写入配置修改记录表
     * 
     * @param fileId
     * @param id
     * @param crudType
     */
    @Override
    public void writeToConfModifyRecord(String fileId, Integer id, Integer crudType) {
        if (crudType == 0) {// 当是添加配置项时
            insertConfModifyRecord(new ConfModifyRecordModel(fileId, id, Short.valueOf("0")));
        } else if (crudType == 1) {// 当是删除配置项时
            ConfModifyRecordModel confModifyRecordModel = selectModifyRecord(fileId, id);
            if (confModifyRecordModel != null) {
                if (confModifyRecordModel.getCrudType() == 2) {// 只有当之前是修改该记录的时候才更改操作类型，如果是增加，则删除操作相对于线上库没有改变
                    confModifyRecordModel.setCrudType(Short.valueOf("1"));// 删除
                    updateModifyRecord(confModifyRecordModel);
                } else if (confModifyRecordModel.getCrudType() == 0) {// 如果这条记录之前是增加的，那么现在这条记录删除了，在配置更改记录表中应该删除
                    deleteModifyRecordById(confModifyRecordModel.getId());
                }
            } else {
                insertConfModifyRecord(new ConfModifyRecordModel(fileId, id, Short.valueOf("1")));
            }
        } else if (crudType == 2) {// 当是修改配置项时
            ConfModifyRecordModel confModifyRecordModel = selectModifyRecord(fileId, id);
            if (confModifyRecordModel == null) {
                // 校验是否真的有修改
                Map map1 = confFileService.selectByTableNameAndId("conffilesdb." + fileId, id);
                Map map2 = confFileService.selectByTableNameAndId("conffilesmanage." + fileId, id);
                map1 = MapDateFormatUtil.formatDateTimeToString(map1);
                map2 = MapDateFormatUtil.formatDateTimeToString(map2);
                int flag = isModified(map1, map2);
                if (flag == 1) {// 当该记录是首次修改且和conffilesdb库内容不同时，标记修改
                    confModifyRecordModel = new ConfModifyRecordModel(fileId, id, Short.valueOf("2"));
                    insertConfModifyRecord(confModifyRecordModel);
                }
            } else if (confModifyRecordModel.getCrudType() == 2) {// 如果之前修改过
                // 校验是否真的有修改
                Map map1 = confFileService.selectByTableNameAndId("conffilesdb." + fileId, id);
                Map map2 = confFileService.selectByTableNameAndId("conffilesmanage." + fileId, id);
                map1 = MapDateFormatUtil.formatDateTimeToString(map1);
                map2 = MapDateFormatUtil.formatDateTimeToString(map2);
                int flag = isModified(map1, map2);
                if (flag == 0) {// conffilesdb和conffilesmanage没有区别
                    deleteModifyRecordById(confModifyRecordModel.getId());
                }
            }
        }
    }

    // 判断map1和map2是否有区别
    private int isModified(Map map1, Map map2) {
        Set<String> columnKeySet = map1.keySet();
        int flag = 0;
        for (String columnKey : columnKeySet) {
            if (!map1.get(columnKey).equals(map2.get(columnKey))) {
                flag = 1;// 确实有修改
            }
        }
        return flag;
    }
}
