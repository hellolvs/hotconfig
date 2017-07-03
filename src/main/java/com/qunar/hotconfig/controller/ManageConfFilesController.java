package com.qunar.hotconfig.controller;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.qunar.flight.qmonitor.QMonitor;
import com.qunar.hotconfig.service.ConfFileListService;
import com.qunar.hotconfig.service.ConfFileService;
import com.qunar.hotconfig.service.UserService;
import com.qunar.hotconfig.service.ValidateConfFileService;
import com.qunar.hotconfig.util.jsonUtil.JsonModel;
import com.qunar.hotconfig.util.jsonUtil.JsonUtils;
import com.qunar.hotconfig.util.validateUtil.ParamException;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;
import java.util.Map;

/**
 * 配置文件管理页面控制器
 *
 * Created by llnn.li on 2017/4/7.
 */
@Controller
@RequestMapping("/manageConfFiles")
public class ManageConfFilesController {

    private static final Logger LOG = LoggerFactory.getLogger(ManageConfFilesController.class);

    @Autowired
    private ConfFileListService confFileListService;
    @Autowired
    private ValidateConfFileService validateConfFileService;
    @Autowired
    private ConfFileService confFileService;
    @Autowired
    private UserService userService;

    /**
     * 配置项页面初始数据（表名和列名）
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/CurrentConfData", method = { RequestMethod.GET })
    public JsonModel setCurrentConfData() {
        return JsonModel.generateJsonModel(confFileListService.selectAllFileIdAndFields());
    }

    /**
     * 查询页面展示信息
     *
     * @param fileId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/CurrentConfData/query", method = { RequestMethod.POST })
    public JsonModel query(@RequestParam("fileId") String fileId, @RequestParam("limit") Integer limit,
            @RequestParam("start") Integer start, @CookieValue("user_info") String key) {
        long startTime = System.currentTimeMillis();
        Preconditions.checkArgument(!Strings.isNullOrEmpty(fileId), "请求参数异常");

        if (fileId.equals("-")) {
            return JsonModel.errJsonModel("请选择有效配置");
        }
        try {
            List<Map> list = confFileService.selectByTableNameAsList(fileId, new RowBounds(start, limit));
            LOG.info("查询配置完成， 配置文件名：{}， 操作人：{}", fileId, userService.getUserInfo(key).getUserId());
            QMonitor.recordOne("Conf_Data_Query", System.currentTimeMillis() - startTime);
            return JsonModel.generateJsonModel(confFileService.count(fileId), list);
        } catch (RuntimeException e) {
            QMonitor.recordOne("Conf_Data_Query_Fail");
            throw e;
        }
    }

    /**
     * 删除数据
     *
     * @param fileId
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/CurrentConfData/delete", method = { RequestMethod.POST })
    public JsonModel delete(@RequestParam("fileId") String fileId, @RequestParam("id") Integer id,
            @CookieValue("user_info") String key) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(fileId) && id >= 0, "请求参数异常");
        try {
            confFileService.deleteById(fileId, id);
        } catch (RuntimeException e) {
            QMonitor.recordOne("Conf_Data_Delete_Fail");
            throw e;
        }
        LOG.info("配置项已删除， 配置文件名：{}， 配置项id：{}， 操作人：{}", fileId, id, userService.getUserInfo(key).getUserId());
        return JsonModel.defaultJsonModel();
    }

    /**
     * 更新数据
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/CurrentConfData/update", method = { RequestMethod.POST })
    public JsonModel update(@RequestParam("fileId") String fileId, @RequestParam("model") String model,
            @CookieValue("user_info") String key) {
        Map columnValuesMap = JsonUtils.json2map(model);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(fileId) && columnValuesMap != null, "请求参数异常");
        validateConfFileService.checkParam(fileId, columnValuesMap);
        try {
            confFileService.updateByPrimaryKey(fileId, columnValuesMap);
        } catch (RuntimeException e) {
            QMonitor.recordOne("Conf_Data_Update_Fail");
            throw e;
        }
        LOG.info("配置项已修改， 配置文件名：{}， 配置项具体值：{}， 操作人：{}", fileId, columnValuesMap,
                userService.getUserInfo(key).getUserId());
        return JsonModel.defaultJsonModel();
    }

    /**
     * 添加数据
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/CurrentConfData/add", method = { RequestMethod.POST })
    public JsonModel insert(@RequestParam("fileId") String fileId, @RequestParam("model") String model,
            @CookieValue("user_info") String key) {
        Map columnValuesMap = JsonUtils.json2map(model);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(fileId) && columnValuesMap != null, "请求参数异常");
        validateConfFileService.checkParam(fileId, columnValuesMap);
        try {
            confFileService.insertByTableName(fileId, columnValuesMap);
        } catch (RuntimeException e) {
            QMonitor.recordOne("Conf_Data_Insert_Fail");
            throw e;
        }
        LOG.info("配置项已添加， 配置文件名：{}， 配置项具体值：{}， 操作人：{}", fileId, columnValuesMap,
                userService.getUserInfo(key).getUserId());
        return JsonModel.defaultJsonModel();
    }

    /* 异常处理，输出异常信息 */
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public JsonModel handlerRuntimeException(RuntimeException e) {
        if (e instanceof ParamException) {
            return JsonModel.errJsonModel(e.getMessage());
        }
        LOG.error("ManageConfFilesController运行时异常：{}", e.getMessage(), e);
        return JsonModel.errJsonModel("后台异常");
    }
}