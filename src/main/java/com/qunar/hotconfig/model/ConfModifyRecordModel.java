package com.qunar.hotconfig.model;

/**
 * Created by shadandan on 17/4/18.
 */
public class ConfModifyRecordModel {

    private Integer id;
    private String fileId;
    private Integer itemId;
    private Short crudType;

    public ConfModifyRecordModel() {
    }

    public ConfModifyRecordModel(String fileId, Integer itemId, Short crudType) {
        this.fileId = fileId;
        this.itemId = itemId;
        this.crudType = crudType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Short getCrudType() {
        return crudType;
    }

    public void setCrudType(Short crudType) {
        this.crudType = crudType;
    }
}
