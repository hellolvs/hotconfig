package com.qunar.hotconfig.dao;

import com.qunar.hotconfig.model.UserPermissionModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shuai.lv
 * @date 2017/4/6.
 */
@Repository
public interface UserPermissionDao {

    List<UserPermissionModel> selectUserPermissionByFileId(String fileId);

    List<UserPermissionModel> selectUserPermissionByUserId(String userId);

    UserPermissionModel selectUserPermission(String userId, String fileId);

    int insertUserPermission(UserPermissionModel userPermissionModel);

    int deleteUserPermissionById(Integer id);

    int updateUserPermissionById(UserPermissionModel userPermissionModel);

}
