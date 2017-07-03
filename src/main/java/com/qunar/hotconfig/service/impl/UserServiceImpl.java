package com.qunar.hotconfig.service.impl;

import com.qunar.hotconfig.dao.UserInfoDao;
import com.qunar.hotconfig.model.UserInfoModel;
import com.qunar.hotconfig.service.MemCacheService;
import com.qunar.hotconfig.service.UserService;
import com.qunar.security.qsso.model.QUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import java.util.UUID;

/**
 * Created by kun.ji on 2017/4/6.
 */
@Service
public class UserServiceImpl implements UserService {

    private final String MEMCACHE_KEY = "user_info_";
    private final String COOKIE_KEY = "user_info";
    private final String INIT_GROUP_ID = "8";// 代表游客身份
    @Autowired
    private MemCacheService memCacheService;
    @Autowired
    private UserInfoDao userInfoDao;

    @Override
    public Cookie saveUserInfo(UserInfoModel userinfo) {
        String key = genUserKey();
        memCacheService.set(MEMCACHE_KEY + key, userinfo);
        return new Cookie(COOKIE_KEY, key);
    }

    @Override
    public UserInfoModel getUserInfo(String key) {
        UserInfoModel ret = (UserInfoModel) memCacheService.get(MEMCACHE_KEY + key);
        return ret;
    }

    @Override
    public UserInfoModel getUserInfoByUserId(String userId) {
        return userInfoDao.selectByPrimaryKey(userId);
    }

    @Override
    public UserInfoModel initNewUser(QUser user) {
        UserInfoModel newUser = new UserInfoModel();
        newUser.setUserId(user.getUserId());
        newUser.setUserName(user.getUserName());
        newUser.setPass("");
        newUser.setGroupId(INIT_GROUP_ID);
        return newUser;
    }

    private static String genUserKey() {
        return UUID.randomUUID().toString();
    }

}