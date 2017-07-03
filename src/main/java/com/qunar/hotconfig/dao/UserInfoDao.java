package com.qunar.hotconfig.dao;

import com.qunar.hotconfig.model.UserInfoModel;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoDao {

    int deleteByPrimaryKey(String userid);

    int insert(UserInfoDao record);

    int insertSelective(UserInfoDao record);

    UserInfoModel selectByPrimaryKey(String userid);

    int updateByPrimaryKeySelective(UserInfoDao record);

    int updateByPrimaryKey(UserInfoDao record);

}