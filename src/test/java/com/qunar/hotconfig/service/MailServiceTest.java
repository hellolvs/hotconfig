package com.qunar.hotconfig.service;

import com.qunar.hotconfig.service.impl.DiffServiceImpl;
import com.qunar.hotconfig.service.impl.MailServiceImpl;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Calendar;


/**
 * Created by shadandan on 17/4/8.
 */
public class MailServiceTest {

    private MailService mailService;

    private DiffService diffService;

    @Before
    public void init() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring/app.xml");
        mailService = applicationContext.getBean(MailServiceImpl.class);
        diffService = applicationContext.getBean(DiffServiceImpl.class);
    }

    @Test
    public void sendMail() {
        String fileId = "flight_info";
        String subject = "配置" + fileId + "修改";
        String userId = "dandan.sha";
        Calendar calendar = Calendar.getInstance();
        String time = new DateTime(calendar).toString("yyyy-MM-dd HH:mm:ss");
        String information = "此次修改可能有较大影响";

        mailService.mailNotification(subject, fileId, userId, time, information, diffService.configDiff(fileId));
    }

}
