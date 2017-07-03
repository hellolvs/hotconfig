package com.qunar.hotconfig.interceptor;

import com.qunar.hotconfig.model.UserInfoModel;
import com.qunar.hotconfig.service.UserService;
import com.qunar.hotconfig.util.userUtil.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by kun.ji on 2017/4/6.
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o)
            throws IOException {
        String url = httpServletRequest.getRequestURI();
        if (url.contains("qsso.html") || url.contains("login")) {
            return true;
        }
        String key = UserUtil.getUserKey(httpServletRequest);
        UserInfoModel user = userService.getUserInfo(key);
        if (user == null) {
            httpServletResponse.sendRedirect("/qsso.html");
            return false;
        }
        return true;
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