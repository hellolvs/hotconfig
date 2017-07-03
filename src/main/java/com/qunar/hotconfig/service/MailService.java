package com.qunar.hotconfig.service;

import com.qunar.hotconfig.util.jsonUtil.DiffResult;
import com.qunar.hotconfig.util.mailUtil.Email;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

/**
 * Created by shadandan on 17/4/8.
 */
public interface MailService {

    /**
     * @param subject 主题
     * @param fileId 修改的配置名（即表名）
     * @param userId 发布人
     * @param time 发布时间
     * @param information 通知信息
     */
    void mailNotification(String subject, String fileId, String userId, String time, String information,
            List<DiffResult> diffResults);

    void sendMail(Email email) throws MessagingException, IOException;

}
