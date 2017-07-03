package com.qunar.hotconfig.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author shuai.lv
 * @date 2017/4/6.
 */
public class UserPermissionModel {

    private Integer id;
    private String userId;
    private String fileId;
    private Boolean modifyPermission;
    private Boolean publishPermission;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public Boolean getModifyPermission() {
        return modifyPermission;
    }

    public void setModifyPermission(Boolean modifyPermission) {
        this.modifyPermission = modifyPermission;
    }

    public Boolean getPublishPermission() {
        return publishPermission;
    }

    public void setPublishPermission(Boolean publishPermission) {
        this.publishPermission = publishPermission;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
