package com.qunar.hotconfig.controller;

import com.qunar.flight.qmonitor.QMonitor;
import com.qunar.hotconfig.model.UserInfoModel;
import com.qunar.hotconfig.model.UserPermissionModel;
import com.qunar.hotconfig.service.UserPermissionService;
import com.qunar.hotconfig.service.UserService;
import com.qunar.hotconfig.util.jsonUtil.JsonModel;
import com.qunar.hotconfig.util.userUtil.UserUtil;
import com.qunar.security.QSSO;
import com.qunar.security.qsso.model.QUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by kun.ji on 2017/4/10.
 */
@Controller
@RequestMapping("")
public class UserInfoController {

    private static final Logger LOG = LoggerFactory.getLogger(UserInfoController.class);

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserService userService;
    @Autowired
    private UserPermissionService userPermissionService;

    @RequestMapping("/login")
    public String login(HttpServletResponse response) {
        String token = request.getParameter("token");
        QUser user = QSSO.getQUser(token);
        UserInfoModel userInfo = userService.getUserInfoByUserId(user.getUserId());
        if (null == userInfo) {
            UserInfoModel newUser = userService.initNewUser(user);
            response.addCookie(userService.saveUserInfo(newUser));
        } else {
            response.addCookie(userService.saveUserInfo(userInfo));
        }
        QMonitor.recordOne("Login_Success");
        LOG.info("用户 {} 已登录", user.getUserId());
        return "redirect:/config.html";
    }

    @ResponseBody
    @RequestMapping("/getUserPermission")
    public JsonModel getUserPermission() {
        String key = UserUtil.getUserKey(request);
        String userId = userService.getUserInfo(key).getUserId();
        List<UserPermissionModel> userPermissions = userPermissionService.selectByUserId(userId);
        return JsonModel.generateJsonModel(userPermissions);

    }

    @ResponseBody
    @RequestMapping("/getUserReadPermission")
    public JsonModel getUserReadPermission() {
        String key = UserUtil.getUserKey(request);
        String userId = userService.getUserInfo(key).getUserId();
        UserInfoModel user = userService.getUserInfoByUserId(userId);
        if (user == null) {
            return JsonModel.errJsonModel("您无查看权限");
        }
        return JsonModel.defaultJsonModel();
    }

    @ResponseBody
    @RequestMapping("/getUserIsAdmin")
    public JsonModel getUserIsAdmin() {
        String key = UserUtil.getUserKey(request);
        String userId = userService.getUserInfo(key).getUserId();
        UserInfoModel user = userService.getUserInfoByUserId(userId);
        if (user != null && user.getGroupId().equals("1")) {
            return JsonModel.defaultJsonModel();
        } else {

            return JsonModel.errJsonModel("您无管理员权限");
        }
    }

    @ResponseBody
    @RequestMapping("/error")
    public JsonModel error(HttpServletRequest httpServletRequest) {

        return JsonModel.errJsonModel((String) httpServletRequest.getAttribute("errMsg"));
    }

    /* 异常处理，输出异常信息 */
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public JsonModel handlerRuntimeException(RuntimeException e) {
        LOG.error("UserInfoController运行时异常：{}", e.getMessage(), e);
        return JsonModel.errJsonModel("无法提供该服务");
    }
}