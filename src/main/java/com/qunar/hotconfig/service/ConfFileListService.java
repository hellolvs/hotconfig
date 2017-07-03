package com.qunar.hotconfig.service;

import com.qunar.hotconfig.util.jsonUtil.ConfListModel;

import java.util.List;

/**
 * @author shuai.lv
 * @date 2017/4/6.
 */
public interface ConfFileListService {

    List<ConfListModel> selectAllFileIdAndFields();

    List selectAllFileId();

}
