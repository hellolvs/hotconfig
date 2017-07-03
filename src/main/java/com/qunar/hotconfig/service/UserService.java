package com.qunar.hotconfig.service;

import com.qunar.hotconfig.model.UserInfoModel;
import com.qunar.security.qsso.model.QUser;

import javax.servlet.http.Cookie;

/**
 * Created by kun.ji on 2017/4/6.
 */
public interface UserService {

    Cookie saveUserInfo(UserInfoModel userinfo);

    UserInfoModel getUserInfo(String key);

    UserInfoModel getUserInfoByUserId(String userId);

    UserInfoModel initNewUser(QUser user);

}