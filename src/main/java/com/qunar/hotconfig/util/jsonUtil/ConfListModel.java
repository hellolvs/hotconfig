package com.qunar.hotconfig.util.jsonUtil;

import java.io.Serializable;
import java.util.List;

/**
 * Created by llnn.li on 2017/4/5.
 */
public class ConfListModel implements Serializable {

    private String fileId;
    private List<TableFieldModel> tableField;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public List<TableFieldModel> getTableField() {
        return tableField;
    }

    public void setTableField(List<TableFieldModel> tableField) {
        this.tableField = tableField;
    }
}
