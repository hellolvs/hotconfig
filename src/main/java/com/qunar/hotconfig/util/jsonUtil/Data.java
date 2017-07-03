package com.qunar.hotconfig.util.jsonUtil;

import java.io.Serializable;
import java.util.List;

/**
 * Created by llnn.li on 2017/4/5.
 */
public class Data implements Serializable {

    // 总数据量
    private int totalCount;
    // 数据集合
    private List list;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

}
