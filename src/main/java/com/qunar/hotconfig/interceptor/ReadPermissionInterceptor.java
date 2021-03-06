package com.qunar.hotconfig.interceptor;

import com.qunar.hotconfig.model.UserInfoModel;
import com.qunar.hotconfig.service.UserService;
import com.qunar.hotconfig.util.userUtil.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by kun.ji on 2017/4/11.
 */
public class ReadPermissionInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) {
        String key = UserUtil.getUserKey(httpServletRequest);
        String userId = userService.getUserInfo(key).getUserId();
        UserInfoModel user = userService.getUserInfoByUserId(userId);
        return user != null;

    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
            ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
            Object o, Exception e) {

    }
}