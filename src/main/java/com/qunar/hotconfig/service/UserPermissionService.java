package com.qunar.hotconfig.service;

import com.qunar.hotconfig.model.UserPermissionModel;

import java.util.List;

/**
 * @author shuai.lv
 * @date 2017/4/6.
 */
public interface UserPermissionService {

    List<UserPermissionModel> selectByFileId(String fileId);

    List<UserPermissionModel> selectByUserId(String userId);

    UserPermissionModel selectByUserIdAndFileId(String userId, String fileId);

    int insert(UserPermissionModel userPermissionModel);

    int deleteById(Integer id);

    int updateById(UserPermissionModel userPermissionModel);

    boolean doubleCheck(UserPermissionModel userPermissionModel);

}
