package com.qunar.hotconfig.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by llnn.li on 2017/4/7.
 */
@Repository
public interface ConfFileDao {

    List<Map> selectByTableName(Map map);

    Map selectByTableNameAndId(Map map);

    List<Map> selectByTableName(Map map, RowBounds rowBounds);

    Integer count(Map map);

    List<String> selectColumns(String fileId);

    int insertByTableName(Map map);

    int batchInsertByTableName(Map map);

    int updateByPrimaryKey(Map map);

    int deleteById(Map map);

    int deleteTable(@Param("fileId") String fileId);

}
