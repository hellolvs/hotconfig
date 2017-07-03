package com.qunar.hotconfig.service.impl;

import com.google.common.collect.Maps;
import com.qunar.hotconfig.dao.ConfFileDao;
import com.qunar.hotconfig.service.ConfFileService;
import com.qunar.hotconfig.service.ConfModifyRecordService;
import com.qunar.hotconfig.util.dateFormatUtil.MapDateFormatUtil;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * Created by llnn.li on 2017/4/7.
 */
@Service
public class ConfFileServiceImpl implements ConfFileService {

    @Autowired
    private ConfFileDao confFileDao;
    @Autowired
    private ConfModifyRecordService confModifyRecordService;

    @Override
    public List<Map> selectByTableName(String fileId) {
        String[] fileIds = fileId.split("\\.");
        List<String> columnList = selectColumns(fileIds.length > 1 ? fileIds[1] : fileId);
        Map<String, Object> map = Maps.newHashMap();
        map.put("fileId", fileId);
        map.put("columnList", columnList);
        return confFileDao.selectByTableName(map);
    }

    @Override
    public Map selectByTableNameAndId(String fileId, Integer id) {
        String[] fileIds = fileId.split("\\.");
        List<String> columnList = selectColumns(fileIds.length > 1 ? fileIds[1] : fileId);
        Map<String, Object> map = Maps.newHashMap();
        map.put("fileId", fileId);
        map.put("id", id);
        map.put("columnList", columnList);
        return confFileDao.selectByTableNameAndId(map);
    }

    @Override
    public List<Map> selectByTableNameAsList(String fileId, RowBounds rowBounds) {
        List<Map> list = selectByTableName(fileId, rowBounds);
        return MapDateFormatUtil.formatDateTimeToString(list);
    }

    @Override
    public List<Map> selectByTableName(String fileId, RowBounds rowBounds) {
        String[] fileIds = fileId.split("\\.");
        List<String> columnList = selectColumns(fileIds.length > 1 ? fileIds[1] : fileId);
        Map<String, Object> map = Maps.newHashMap();
        map.put("fileId", fileId);
        map.put("columnList", columnList);
        return confFileDao.selectByTableName(map, rowBounds);
    }

    @Override
    public List<String> selectColumns(String fileId) {
        return confFileDao.selectColumns(fileId);
    }

    @Override
    public Integer count(String fileId) {
        Map<String, String> map = Maps.newHashMap();
        map.put("fileId", fileId);
        return confFileDao.count(map);
    }

    @Transactional
    @Override
    public int insertByTableName(String fileId, Map columnValuesMap) {
        Map<String, Object> map = Maps.newHashMap();
        String[] insertSqlValues = joinInsertSqlValues(fileId, columnValuesMap);
        map.put("id", null);
        map.put("fileId", fileId);
        map.put("columns", insertSqlValues[0]);
        map.put("columnsValues", insertSqlValues[1]);
        int result = confFileDao.insertByTableName(map);
        // 写入配置修改记录表
        confModifyRecordService.writeToConfModifyRecord(fileId, (Integer) map.get("id"), 0);
        return result;
    }

    @Override
    public int batchInsertByTableName(String fileId, List<Map> mapList) {
        Map<String, Object> map = Maps.newHashMap();
        String[] batchInsertSqlValues = joinBatchInsertSqlValues(fileId, mapList);

        map.put("fileId", fileId);
        map.put("columns", batchInsertSqlValues[0]);
        map.put("columnsValues", batchInsertSqlValues[1]);
        return confFileDao.batchInsertByTableName(map);
    }

    @Transactional
    @Override
    public int updateByPrimaryKey(String fileId, Map columnValuesMap) {
        Map<String, Object> map = Maps.newHashMap();
        String updateSqlValues = joinUpdateSqlValues(fileId, columnValuesMap);

        Integer id = (Integer) columnValuesMap.get("id");
        map.put("fileId", fileId);
        map.put("id", id);
        map.put("updateValues", updateSqlValues);
        int num = confFileDao.updateByPrimaryKey(map);
        // 写入配置修改记录表
        confModifyRecordService.writeToConfModifyRecord(fileId, id, 2);
        return num;
    }

    @Transactional
    @Override
    public int deleteById(String fileId, Integer id) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("fileId", fileId);
        map.put("id", id);
        int num = confFileDao.deleteById(map);
        // 写入配置修改记录表
        confModifyRecordService.writeToConfModifyRecord(fileId, id, 1);
        return num;
    }

    @Override
    public int deleteTable(String fileId) {
        return confFileDao.deleteTable(fileId);
    }

    /**
     * 拼装插入sql语句 INSERT INTO ${fileId} (${columns}) VALUES ${columnsValues};
     *
     * @param fileId 表名
     * @param columnValuesMap 参数值
     * @return 插入sql的变量 insertSqlValues[0]==${columns} insertSqlValues[1]==(${columnsValues})
     */
    public String[] joinInsertSqlValues(String fileId, Map columnValuesMap) {

        String[] insertSqlValues = new String[2];
        String[] fileIds = fileId.split("\\.");
        if (fileIds.length > 1) {
            fileId = fileIds[1];
        }
        List<String> columnList = selectColumns(fileId);

        StringBuilder columnsBuilder = new StringBuilder();
        StringBuilder builder = new StringBuilder();
        for (String key : columnList) {
            if (key.equals("id")) {
                continue;
            }
            if (columnValuesMap.containsKey(key)) {
                Object object = columnValuesMap.get(key);
                if (object instanceof String || object instanceof Date || object instanceof Time
                        || object instanceof Timestamp) {
                    builder.append("\'");
                    builder.append(columnValuesMap.get(key));
                    builder.append("\'");
                } else {
                    builder.append(columnValuesMap.get(key));
                }
                builder.append(",");
                columnsBuilder.append(key);
                columnsBuilder.append(",");
            }
        }
        builder.deleteCharAt(builder.length() - 1);
        columnsBuilder.deleteCharAt(columnsBuilder.length() - 1);

        insertSqlValues[0] = columnsBuilder.toString();
        insertSqlValues[1] = builder.toString();
        return insertSqlValues;
    }

    /**
     * 拼装批量插入sql语句 INSERT INTO ${fileId} (${columns}) VALUES ${columnsValues};
     *
     * @param fileId 表名
     * @param mapList 批量插入的多行数据
     * @return 插入sql的变量 batchInsertSqlValues[0]==${columns} batchInsertSqlValues[1]==(${columnsValues})
     */
    public String[] joinBatchInsertSqlValues(String fileId, List<Map> mapList) {

        String[] batchInsertSqlValues = new String[2];
        String[] fileIds = fileId.split("\\.");
        if (fileIds.length > 1) {
            fileId = fileIds[1];
        }
        List<String> columnList = selectColumns(fileId);

        StringBuilder columnsBuilder = new StringBuilder();
        StringBuilder builder = new StringBuilder();
        for (String key : columnList) {
            columnsBuilder.append(key);
            columnsBuilder.append(",");
        }
        for (Map map : mapList) {
            builder.append("(");
            for (String key : columnList) {
                Object object = map.get(key);
                if (object instanceof String || object instanceof Date || object instanceof Time
                        || object instanceof Timestamp) {
                    builder.append("\'");
                    builder.append(map.get(key));
                    builder.append("\'");
                } else {
                    builder.append(map.get(key));
                }
                builder.append(",");
            }
            builder.deleteCharAt(builder.length() - 1);
            builder.append("),");
        }
        builder.deleteCharAt(builder.length() - 1);
        columnsBuilder.deleteCharAt(columnsBuilder.length() - 1);

        batchInsertSqlValues[0] = columnsBuilder.toString();
        batchInsertSqlValues[1] = builder.toString();
        return batchInsertSqlValues;
    }

    /**
     * 拼装更新sql语句 update ${fileId} set ${updateValues} where id = #{id}
     *
     * @param fileId 表名
     * @param columnValuesMap 参数值
     * @return 更新sql的变量updateSqlValues==${updateValues}
     */
    public String joinUpdateSqlValues(String fileId, Map columnValuesMap) {
        String[] fileIds = fileId.split("\\.");
        if (fileIds.length > 1) {
            fileId = fileIds[1];
        }
        List<String> columnList = selectColumns(fileId);

        StringBuilder builder = new StringBuilder();
        for (String key : columnList) {
            if (key.equals("id")) {
                continue;
            }
            if (columnValuesMap.containsKey(key)) {
                builder.append(key).append("=");
                Object object = columnValuesMap.get(key);
                if (object instanceof String || object instanceof Date || object instanceof Time
                        || object instanceof Timestamp) {
                    builder.append("\'");
                    builder.append(columnValuesMap.get(key));
                    builder.append("\'");
                } else {
                    builder.append(columnValuesMap.get(key));
                }
                builder.append(",");
            }
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

}
