package com.qunar.hotconfig.dao;

import com.qunar.hotconfig.model.ConfModifyRecordModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by shadandan on 17/4/18.
 */
@Repository
public interface ConfModifyRecordDao {

    List<ConfModifyRecordModel> selectModifyRecordByFileId(String fileId);

    ConfModifyRecordModel selectModifyRecord(String fileId, Integer itemId);

    int insertConfModifyRecord(ConfModifyRecordModel confModifyRecordModel);

    int deleteModifyRecordByFileId(String fileId);

    int deleteModifyRecordById(Integer id);

    int updateModifyRecord(ConfModifyRecordModel confModifyRecordModel);

}
