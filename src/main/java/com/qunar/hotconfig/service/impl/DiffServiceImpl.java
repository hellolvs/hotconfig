package com.qunar.hotconfig.service.impl;

import com.google.common.collect.Lists;
import com.qunar.hotconfig.model.ConfModifyRecordModel;
import com.qunar.hotconfig.service.ConfFileService;
import com.qunar.hotconfig.service.ConfModifyRecordService;
import com.qunar.hotconfig.service.DiffService;
import com.qunar.hotconfig.util.dateFormatUtil.MapDateFormatUtil;
import com.qunar.hotconfig.util.jsonUtil.DiffResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by shadandan on 17/4/5.
 */
@Service
public class DiffServiceImpl implements DiffService {

    @Autowired
    private ConfFileService confFileService;
    @Autowired
    private ConfModifyRecordService confModifyRecordService;

    @Override
    public List<DiffResult> configDiff(String fileId) {
        List<ConfModifyRecordModel> confModifyRecordModelList = confModifyRecordService
                .selectModifyRecordByFileId(fileId);
        List<DiffResult> diffResults = Lists.newArrayList();
        for (ConfModifyRecordModel confModifyRecordModel : confModifyRecordModelList) {
            Short crudType = confModifyRecordModel.getCrudType();
            Integer itemId = confModifyRecordModel.getItemId();
            if (crudType == 0) {// 增加
                Map map = confFileService.selectByTableNameAndId("conffilesmanage." + fileId, itemId);
                if (map != null) {
                    map = MapDateFormatUtil.formatDateTimeToString(map);
                    DiffResult diffResult = new DiffResult(0, map);
                    diffResults.add(diffResult);
                }
            } else if (crudType == 1) {// 删除
                Map map = confFileService.selectByTableNameAndId("conffilesdb." + fileId, itemId);
                if (map != null) {
                    map = MapDateFormatUtil.formatDateTimeToString(map);
                    DiffResult diffResult = new DiffResult(1, map);
                    diffResults.add(diffResult);
                }
            } else if (crudType == 2) {// 修改
                Map map1 = confFileService.selectByTableNameAndId("conffilesdb." + fileId, itemId);
                Map map2 = confFileService.selectByTableNameAndId("conffilesmanage." + fileId, itemId);
                if (map1 != null && map2 != null) {
                    map1 = MapDateFormatUtil.formatDateTimeToString(map1);
                    map2 = MapDateFormatUtil.formatDateTimeToString(map2);

                    // 存储修改的字段的集合
                    List<String> columnList = Lists.newArrayList();
                    Set<String> columnKeySet = map1.keySet();

                    for (String columnKey : columnKeySet) {
                        if (!map1.get(columnKey).equals(map2.get(columnKey))) {
                            columnList.add(columnKey);
                        }
                    }
                    DiffResult diffResult1 = new DiffResult(2, map1, columnList);// 修改前
                    diffResults.add(diffResult1);

                    DiffResult diffResult2 = new DiffResult(3, map2, columnList);// 修改后
                    diffResults.add(diffResult2);
                }
            }
        }
        return diffResults;
    }
}
