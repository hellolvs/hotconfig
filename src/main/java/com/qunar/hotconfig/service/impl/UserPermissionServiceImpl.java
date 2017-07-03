package com.qunar.hotconfig.service.impl;

import com.qunar.hotconfig.dao.UserPermissionDao;
import com.qunar.hotconfig.model.UserPermissionModel;
import com.qunar.hotconfig.service.UserPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shuai.lv
 * @date 2017/4/6.
 */
@Service
public class UserPermissionServiceImpl implements UserPermissionService {

    @Autowired
    private UserPermissionDao userPermissionDao;

    @Override
    public List<UserPermissionModel> selectByFileId(String fileId) {
        return userPermissionDao.selectUserPermissionByFileId(fileId);
    }

    @Override
    public List<UserPermissionModel> selectByUserId(String userId) {
        return userPermissionDao.selectUserPermissionByUserId(userId);
    }

    @Override
    public UserPermissionModel selectByUserIdAndFileId(String userId, String fileId) {
        return userPermissionDao.selectUserPermission(userId, fileId);
    }

    @Override
    public int insert(UserPermissionModel userPermissionModel) {
        return userPermissionDao.insertUserPermission(userPermissionModel);
    }

    @Override
    public int deleteById(Integer id) {
        return userPermissionDao.deleteUserPermissionById(id);
    }

    @Override
    public int updateById(UserPermissionModel userPermissionModel) {
        return userPermissionDao.updateUserPermissionById(userPermissionModel);
    }

    @Override
    public boolean doubleCheck(UserPermissionModel userPermissionModel) {
        if (userPermissionModel.getModifyPermission() && userPermissionModel.getPublishPermission()) {
            return false;
        }
        return true;
    }
}
