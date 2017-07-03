package com.qunar.hotconfig.util.jsonUtil;

import java.io.Serializable;

/**
 * Created by llnn.li on 2017/4/6.
 */
public class TableFieldModel implements Serializable {
    private String title;
    private String dataIndex;
    private Integer widthLen;

    public TableFieldModel(String column) {
        this.title = column;
        this.dataIndex = column;
        this.widthLen = 50;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDataIndex() {
        return dataIndex;
    }

    public void setDataIndex(String dataIndex) {
        this.dataIndex = dataIndex;
    }

    public Integer getWidthLen() {
        return widthLen;
    }

    public void setWidthLen(Integer widthLen) {
        this.widthLen = widthLen;
    }
}
