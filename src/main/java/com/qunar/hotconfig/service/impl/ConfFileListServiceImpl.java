package com.qunar.hotconfig.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.qunar.hotconfig.dao.ConfFileDao;
import com.qunar.hotconfig.dao.ConfFileListDao;
import com.qunar.hotconfig.service.ConfFileListService;
import com.qunar.hotconfig.util.jsonUtil.ConfListModel;
import com.qunar.hotconfig.util.jsonUtil.TableFieldModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

/**
 * @author shuai.lv
 * @date 2017/4/6.
 */
@Service
public class ConfFileListServiceImpl implements ConfFileListService {

    @Autowired
    private ConfFileListDao confFileListDao;
    @Autowired
    private ConfFileDao confFileDao;

    @Override
    public List selectAllFileId() {
        List<String> list = confFileListDao.selectAllFileId();
        List<Map<String, String>> mapList = Lists.newArrayList();
        for (String s : list) {
            Map<String, String> map = Maps.newHashMap();
            map.put("fileId", s);
            mapList.add(map);
        }
        return mapList;
    }

    @Override
    public List<ConfListModel> selectAllFileIdAndFields() {
        List<ConfListModel> list = Lists.newArrayList();
        List<String> fileIdList = confFileListDao.selectAllFileId();
        List<String> columnsList;
        List<TableFieldModel> tableFieldModels;

        for (String fileId : fileIdList) {
            ConfListModel confListModel = new ConfListModel();
            columnsList = confFileDao.selectColumns(fileId);

            tableFieldModels = Lists.newArrayList();
            for (String column : columnsList) {
                tableFieldModels.add(new TableFieldModel(column));
            }
            confListModel.setFileId(fileId);
            confListModel.setTableField(tableFieldModels);
            list.add(confListModel);
        }
        return list;
    }
}
