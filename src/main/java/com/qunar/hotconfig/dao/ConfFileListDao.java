package com.qunar.hotconfig.dao;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shuai.lv
 * @date 2017/4/6.
 */
@Repository
public interface ConfFileListDao {

    List<String> selectAllFileId();

}
