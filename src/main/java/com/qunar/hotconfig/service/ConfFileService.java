package com.qunar.hotconfig.service;

import org.apache.ibatis.session.RowBounds;
import java.util.List;
import java.util.Map;

/**
 * Created by llnn.li on 2017/4/7.
 */
public interface ConfFileService {

    List<Map> selectByTableName(String fileId);

    Map selectByTableNameAndId(String fileId, Integer id);

    List<Map> selectByTableName(String fileId, RowBounds rowBounds);

    List<String> selectColumns(String fileId);

    Integer count(String fileId);

    int insertByTableName(String fileId, Map columnValuesMap);

    int batchInsertByTableName(String fileId, List<Map> mapList);

    int updateByPrimaryKey(String fileId, Map columnValuesMap);

    int deleteById(String fileId, Integer id);

    int deleteTable(String fileId);

    List<Map> selectByTableNameAsList(String fileId, RowBounds rowBounds);

}
