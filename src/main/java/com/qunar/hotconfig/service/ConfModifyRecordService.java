package com.qunar.hotconfig.service;

import com.qunar.hotconfig.model.ConfModifyRecordModel;

import java.util.List;

/**
 * Created by shadandan on 17/4/18.
 */
public interface ConfModifyRecordService {

    List<ConfModifyRecordModel> selectModifyRecordByFileId(String fileId);

    ConfModifyRecordModel selectModifyRecord(String fileId, Integer itemId);

    int insertConfModifyRecord(ConfModifyRecordModel confModifyRecordModel);

    int deleteModifyRecordByFileId(String fileId);

    int deleteModifyRecordById(Integer id);

    int updateModifyRecord(ConfModifyRecordModel confModifyRecordModel);

    void writeToConfModifyRecord(String fileId, Integer id, Integer crudType);

}
