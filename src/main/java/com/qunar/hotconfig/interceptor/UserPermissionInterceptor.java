package com.qunar.hotconfig.interceptor;

import com.qunar.hotconfig.model.UserPermissionModel;
import com.qunar.hotconfig.service.UserPermissionService;
import com.qunar.hotconfig.service.UserService;
import com.qunar.hotconfig.util.userUtil.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by kun.ji on 2017/4/6.
 */
public class UserPermissionInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;
    @Autowired
    private UserPermissionService userPermissionService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o)
            throws ServletException, IOException {
        String URI = httpServletRequest.getRequestURI();
        // 发布权限
        if (URI.contains("publish")) {
            UserPermissionModel userPermission = getUserPermission(httpServletRequest);
            if (userPermission != null && userPermission.getPublishPermission()) {
                return true;
            } else {
                httpServletRequest.setAttribute("errMsg", "您无发布权限");
                forwardError(httpServletRequest, httpServletResponse);
                return false;
            }
            // 修改权限
        } else if (URI.contains("add") || URI.contains("delete") || URI.contains("update")) {
            UserPermissionModel userPermission = getUserPermission(httpServletRequest);
            if (userPermission != null && userPermission.getModifyPermission()) {
                return true;
            } else {
                httpServletRequest.setAttribute("errMsg", "您无编辑权限");
                forwardError(httpServletRequest, httpServletResponse);
                return false;
            }
            // 其他URL
        } else {
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
            ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
            Object o, Exception e) {

    }

    private UserPermissionModel getUserPermission(HttpServletRequest httpServletRequest) {
        String key = UserUtil.getUserKey(httpServletRequest);
        String userId = userService.getUserInfo(key).getUserId();
        String fileId = httpServletRequest.getParameter("fileId");
        return userPermissionService.selectByUserIdAndFileId(userId, fileId);
    }

    private void forwardError(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = "/error";
        request.getRequestDispatcher(url).forward(request, response);
    }
}