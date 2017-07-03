package com.qunar.hotconfig.util.jsonUtil;

import java.io.Serializable;
import java.util.List;

/**
 * Created by shadandan on 17/4/5.
 */
public class DiffResult implements Serializable {

    private Integer crudType;
    private Object model;
    private List<String> columnArr;

    public DiffResult() {
    }

    public DiffResult(Integer crudType, Object model, List<String> columnArr) {
        this.crudType = crudType;
        this.model = model;
        this.columnArr = columnArr;
    }

    public DiffResult(Integer crudType, Object model) {
        this.crudType = crudType;
        this.model = model;
    }

    public Integer getCrudType() {
        return crudType;
    }

    public void setCrudType(Integer crudType) {
        this.crudType = crudType;
    }

    public Object getModel() {
        return model;
    }

    public void setModel(Object model) {
        this.model = model;
    }

    public List<String> getColumnArr() {
        return columnArr;
    }

    public void setColumnArr(List<String> columnArr) {
        this.columnArr = columnArr;
    }
}
