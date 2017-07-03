package com.qunar.hotconfig.controller;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.qunar.hotconfig.model.UserPermissionModel;
import com.qunar.hotconfig.service.ConfFileListService;
import com.qunar.hotconfig.service.UserPermissionService;
import com.qunar.hotconfig.service.UserService;
import com.qunar.hotconfig.util.jsonUtil.JsonModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 用户权限管理页面控制器
 *
 * @author shuai.lv
 * @date 2017/4/6.
 */
@Controller
@RequestMapping("/manageUserPermission/CurrentUserPermission")
public class UserPermissionController {

    private static final Logger LOG = LoggerFactory.getLogger(UserPermissionController.class);

    @Autowired
    private UserPermissionService userPermissionService;
    @Autowired
    private ConfFileListService confFileListService;
    @Autowired
    private UserService userService;

    /* 获取所有配置文件表名 */
    @RequestMapping(method = { RequestMethod.GET })
    @ResponseBody
    public JsonModel getConfFileList() {
        return JsonModel.generateJsonModel(confFileListService.selectAllFileId());
    }

    /* 显示某一配置文件的所有用户权限信息 */
    @RequestMapping(value = "/query", method = { RequestMethod.POST })
    @ResponseBody
    public JsonModel list(@RequestParam String fileId, @CookieValue("user_info") String key) {
        Preconditions.checkNotNull(fileId, "接收参数为空");
        if (fileId.equals("-")) {
            return JsonModel.errJsonModel("请选择有效配置");
        }
        LOG.info("查询{}配置下的用户权限信息， 操作人：{}", fileId, userService.getUserInfo(key).getUserId());
        return JsonModel.generateJsonModel(userPermissionService.selectByFileId(fileId));
    }

    /* 添加用户权限 */
    @RequestMapping(value = "/add", method = { RequestMethod.POST })
    @ResponseBody
    public JsonModel add(@RequestBody UserPermissionModel userPermissionModel, @CookieValue("user_info") String key) {
        Preconditions.checkNotNull(userPermissionModel, "接收参数为空");

        if (Strings.isNullOrEmpty(userPermissionModel.getUserId())) {
            return JsonModel.errJsonModel("请输入用户名");
        }

        if (!userPermissionModel.getUserId().matches("[a-z][a-z.]{2,19}")) {
            return JsonModel.errJsonModel("用户名不合法：用户名须由小写字母和.组成且开头必须是小写字母，总长度须在3-20位");
        }

        if (userPermissionService.selectByUserIdAndFileId(userPermissionModel.getUserId(),
                userPermissionModel.getFileId()) != null) {
            return JsonModel.errJsonModel("该用户对该配置的权限定义已存在，无法添加，请使用修改或删除操作");
        }

        // Double Check机制，校验用户不能兼具修改和发布权限
        if (!userPermissionService.doubleCheck(userPermissionModel)) {
            return JsonModel.errJsonModel("违反Double Check规则：同一用户不能兼具修改和发布权限");
        }

        userPermissionService.insert(userPermissionModel);
        LOG.info("添加用户权限完成， 添加的具体用户权限信息：{}， 操作人：{}", userPermissionModel,
                userService.getUserInfo(key).getUserId());
        return JsonModel.defaultJsonModel();
    }

    /* 删除用户权限 */
    @RequestMapping(value = "/delete", method = { RequestMethod.POST })
    @ResponseBody
    public JsonModel delete(@RequestBody Map paramMap, @CookieValue("user_info") String key) {
        Integer id = (Integer) paramMap.get("id");
        Preconditions.checkNotNull(id);
        userPermissionService.deleteById(id);
        LOG.info("删除用户权限完成， 删除的用户权限id：{}， 操作人：{}", id, userService.getUserInfo(key).getUserId());
        return JsonModel.defaultJsonModel();
    }

    /* 修改用户权限 */
    @RequestMapping(value = "/update", method = { RequestMethod.POST })
    @ResponseBody
    public JsonModel update(@RequestBody UserPermissionModel userPermissionModel,
            @CookieValue("user_info") String key) {
        Preconditions.checkNotNull(userPermissionModel, "接收参数为空");

        // Double Check机制，校验用户不能兼具修改和发布权限
        if (!userPermissionService.doubleCheck(userPermissionModel)) {
            return JsonModel.errJsonModel("违反Double Check规则：同一用户不能兼具修改和发布权限");
        }

        userPermissionService.updateById(userPermissionModel);
        LOG.info("修改用户权限信息完成， 修改后的具体用户权限信息：{}， 操作人：{}", userPermissionModel,
                userService.getUserInfo(key).getUserId());
        return JsonModel.defaultJsonModel();
    }

    /* 异常处理，输出异常信息 */
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public JsonModel handlerRuntimeException(RuntimeException e) {
        LOG.error("UserPermissionController运行时异常：{}", e.getMessage(), e);
        return JsonModel.errJsonModel("后台异常");
    }
}
