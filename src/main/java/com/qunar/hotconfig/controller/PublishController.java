package com.qunar.hotconfig.controller;

import com.google.common.base.Preconditions;
import com.qunar.flight.qmonitor.QMonitor;
import com.qunar.hotconfig.service.ConfModifyRecordService;
import com.qunar.hotconfig.service.DiffService;
import com.qunar.hotconfig.service.MailService;
import com.qunar.hotconfig.service.PublishService;
import com.qunar.hotconfig.service.UserService;
import com.qunar.hotconfig.util.jsonUtil.DiffResult;
import com.qunar.hotconfig.util.jsonUtil.JsonModel;
import org.joda.time.DateTime;
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

import java.util.Date;
import java.util.List;

/**
 * 发布配置控制器
 *
 * @author shuai.lv
 * @date 2017/4/9.
 */
@Controller
@RequestMapping("/manageConfFiles/CurrentConfData/publish")
public class PublishController {

    private static final Logger LOG = LoggerFactory.getLogger(PublishController.class);

    @Autowired
    private PublishService publishService;
    @Autowired
    private MailService mailService;
    @Autowired
    private UserService userService;
    @Autowired
    private DiffService diffService;
    @Autowired
    private ConfModifyRecordService confModifyRecordService;

    /* 发布配置 */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public JsonModel publish(@RequestParam("fileId") String fileId, @CookieValue("user_info") String key) {
        long startTime = System.currentTimeMillis();
        /* 参数校验 */
        String userId = userService.getUserInfo(key).getUserId();
        Preconditions.checkNotNull(userId, "用户ID为空");
        Preconditions.checkNotNull(fileId, "配置文件ID为空");

        /* 对比差异内容，为空则不执行发布 */
        List<DiffResult> diffResults = diffService.configDiff(fileId);
        if (diffResults == null || diffResults.size() == 0) {
            return JsonModel.errJsonModel("跟线上库没有差异内容，无法发布");
        }

        /* 发布配置 */
        long time = publishService.publishConfFile(userId, fileId);
        LOG.info("发布配置成功， 发布的配置文件：{}， 发布人：{}", fileId, userId);

        // 删除配置修改记录表中对应配置的修改记录
        confModifyRecordService.deleteModifyRecordByFileId(fileId);

        /* 邮件周知 */
        Date date = new Date(time);
        String publishTime = new DateTime(date).toString("yyyy-MM-dd HH:mm:ss");
        String subject = "配置" + fileId + "修改";
        String information = "此次修改可能有较大影响";
        mailService.mailNotification(subject, fileId, userId, publishTime, information, diffResults);
        QMonitor.recordOne("Publish", System.currentTimeMillis() - startTime);
        return JsonModel.defaultJsonModel();
    }

    /* 异常处理，输出异常信息 */
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public JsonModel handlerRuntimeException(RuntimeException e) {
        LOG.error("PublishController运行时异常：{}", e.getMessage(), e);
        return JsonModel.errJsonModel("后台异常");
    }
}