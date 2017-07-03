package com.qunar.hotconfig.service.impl;

import com.google.common.collect.Maps;
import com.qunar.hotconfig.service.ConfFileService;
import com.qunar.hotconfig.service.MailService;
import com.qunar.hotconfig.util.jsonUtil.DiffResult;
import com.qunar.hotconfig.util.mailUtil.Email;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;
import qunar.tc.qconfig.client.spring.QConfig;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by shadandan on 17/4/8.
 */
@Service
public class MailServiceImpl implements MailService {

    private static final Logger LOG = LoggerFactory.getLogger(MailServiceImpl.class);

    @Autowired
    private JavaMailSenderImpl mailSender;// 注入Spring封装的javamail，Spring的xml中已让框架装配
    @Autowired
    private ConfFileService confFileService;
    @Autowired
    private VelocityEngine velocityEngine;

    @QConfig("monitor_config.properties")
    private Map<String, String> map;

    /**
     * @param subject 主题
     * @param fileId 修改的配置名（即表名）
     * @param userId 发布人
     * @param time 发布时间
     * @param information 通知信息
     */
    @Override
    public void mailNotification(String subject, String fileId, String userId, String time, String information,
            List<DiffResult> diffResults) {

        List<String> columnList = confFileService.selectColumns(fileId);
        Map<String, Object> velocityMailParameter = Maps.newHashMap();
        velocityMailParameter.put("information", information);
        velocityMailParameter.put("columnList", columnList);
        velocityMailParameter.put("diffResults", diffResults);
        velocityMailParameter.put("userId", userId);
        velocityMailParameter.put("time", time);

        // velocity模板生成html内容
        String content = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "template/mail.vm",
                velocityMailParameter);

        String fromAddress = map.get("mail.from");
        String[] toAddress = map.get("mail.to.many").split(",");
        Email email = new Email();
        email.setFromAddress(fromAddress);
        email.setToAddress(toAddress);
        email.setSubject(subject);
        email.setContent(content);

        try {
            sendMail(email);
        } catch (MessagingException e) {
            LOG.error("发送邮件过程失败", e);
        } catch (IOException e) {
            LOG.error("发送邮件失败", e);
        }
    }

    @Override
    public void sendMail(Email email) throws MessagingException, IOException {

        MimeMessage mime = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mime, true, "utf-8");
        helper.setFrom(email.getFromAddress());// 发件人
        helper.setTo(email.getToAddress());// 收件人
        helper.setReplyTo(email.getFromAddress());// 回复到
        helper.setSubject(email.getSubject());// 邮件主题
        helper.setText(email.getContent(), true);// true表示设定html格式
        mailSender.send(mime);

    }

}