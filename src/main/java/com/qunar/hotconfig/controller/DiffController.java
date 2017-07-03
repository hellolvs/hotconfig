package com.qunar.hotconfig.controller;

import com.google.common.base.Preconditions;
import com.qunar.flight.qmonitor.QMonitor;
import com.qunar.hotconfig.service.DiffService;
import com.qunar.hotconfig.service.UserService;
import com.qunar.hotconfig.util.jsonUtil.JsonModel;
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

/**
 * 对比配置差异（diff）控制器
 *
 * Created by shadandan on 17/4/6.
 */
@Controller
@RequestMapping("/manageConfFiles")
public class DiffController {

    private static final Logger LOG = LoggerFactory.getLogger(DiffController.class);

    @Autowired
    private DiffService diffService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/CurrentConfData/diff", method = { RequestMethod.POST })
    @ResponseBody
    public JsonModel diff(@RequestParam("fileId") String fileId, @CookieValue("user_info") String key) {
        Preconditions.checkNotNull(fileId, "配置文件名不能为空");
        long startTime = System.currentTimeMillis();
        List list = diffService.configDiff(fileId);
        QMonitor.recordOne("Diff", System.currentTimeMillis() - startTime);
        LOG.info("diff操作完成， 配置文件名：{}， 操作人：{}", fileId, userService.getUserInfo(key).getUserId());
        return JsonModel.generateJsonModel(list);
    }

    /* 异常处理，输出异常信息 */
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public JsonModel handlerRuntimeException(RuntimeException e) {
        LOG.error("DiffController运行时异常：{}", e.getMessage(), e);
        return JsonModel.errJsonModel("后台异常");
    }
}
